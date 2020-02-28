package com.imooc.service.impl;

import com.imooc.enums.MsgAction;
import com.imooc.enums.MsgSignFlag;
import com.imooc.enums.SearchFriendsStatus;
import com.imooc.fastDFS.FastDFSClient;
import com.imooc.fastDFS.FileUtils;
import com.imooc.mapper.*;
import com.imooc.pojo.ChatRecord;
import com.imooc.pojo.Friends;
import com.imooc.pojo.FriendsRequest;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.FriendRequestVO;
import com.imooc.pojo.vo.MyFriendsVO;
import com.imooc.service.UserService;
import com.imooc.service.websocket.UserChannelRel;
import com.imooc.service.websocket.pojo.DataContent;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.QRCodeUtils;
import com.imooc.service.websocket.pojo.ChatMsg;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private FriendsMapper friendsMapper;

    @Resource
    private FriendsRequestMapper friendsRequestMapper;

    @Resource
    private UsersExpandMapper usersExpandMapper;

    @Resource
    private ChatRecordMapper chatRecordMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Users userIsExist(Users user) {
        return usersMapper.selectOne(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String addUser(String password, String cid) throws IOException {
        String username = usersMapper.getUsername();
        if (StringUtils.isBlank(username)) {
            return null;
        }
        Users user = new Users();
        user.setId(sid.nextShort());
        user.setUsername(username);
        user.setNickname("ID:" + username);
        user.setPassword(password);
        //为每个用户生成二维码
        String qrCodePath = "D:\\_plpl_piliao\\" + user.getId() + "\\qrCode\\" + "qrCode.png";
        File qrCodeFile = new File(qrCodePath);
        if (qrCodeFile.getParentFile() == null || !qrCodeFile.getParentFile().isDirectory()) {
            qrCodeFile.getParentFile().mkdirs();
        }
        qrCodeUtils.createQRCode(qrCodePath,"qrCode:" + user.getUsername());
        MultipartFile file = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = fastDFSClient.uploadQRCode(file);
        user.setQrcode(qrCodeUrl);
        user.setCid(cid);
        usersMapper.insert(user);
        usersMapper.deleteUsername(username);
        return username;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUser(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Users getUser(String id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        Users friend = queryUserByUsername(friendUsername);
        // 搜索的用户如果不存在,返回[无此用户]
        if (friend == null) {
            return SearchFriendsStatus.USER_NOT_EXIST.status;
        }
        // 搜索的用户是自己,返回[不能添加自己]
        if (friend.getId().equals(myUserId)) {
            return SearchFriendsStatus.NOT_YOURSELF.status;
        }
        // 搜索的用户已经是好友,返回[该用户已经是你的好友]
        Example example = new Example(Friends.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",myUserId);
        criteria.andEqualTo("friendId",friend.getId());
        if (friendsMapper.selectOneByExample(example) != null) {
            return SearchFriendsStatus.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatus.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Users queryUserByUsername(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        return usersMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendId) {
        // 查询发送好友请求记录表
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId",myUserId);
        criteria.andEqualTo("receiveUserId",friendId);
        // 如果没有好友请求记录,则新增好友请求记录
        if (friendsRequestMapper.selectOneByExample(example) == null) {
            FriendsRequest friendsRequest = new FriendsRequest();
            friendsRequest.setId(sid.nextShort());
            friendsRequest.setSendUserId(myUserId);
            friendsRequest.setReceiveUserId(friendId);
            friendsRequest.setRequestDateTime(new Date());
            friendsRequestMapper.insert(friendsRequest);
            //使用webSocket主动推送消息到请求接收者,更新他的加好友请求
            Channel channel = UserChannelRel.getChannel(friendId);
            if (channel != null) {
                DataContent dataContent  = new DataContent();
                dataContent.setAction(MsgAction.REQUEST_FRIEND.type);
                channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String userId) {
        return usersExpandMapper.queryFriendRequestList(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteFriendRequest(String sendUserId, String receiveUserId) {
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId",sendUserId);
        criteria.andEqualTo("receiveUserId",receiveUserId);
        friendsRequestMapper.deleteByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveFriendRequest(String sendUserId, String receiveUserId) {
        Friends friends = new Friends();
        friends.setId(sid.nextShort());
        friends.setUserId(sendUserId);
        friends.setFriendId(receiveUserId);
        friendsMapper.insert(friends);
        friends.setId(sid.nextShort());
        friends.setUserId(receiveUserId);
        friends.setFriendId(sendUserId);
        friendsMapper.insert(friends);
        //使用webSocket主动推送消息到请求发起者,更新他的通讯录列表为最新
        Channel channel = UserChannelRel.getChannel(sendUserId);
        if (channel != null) {
            DataContent dataContent  = new DataContent();
            dataContent.setAction(MsgAction.AGREE_FRIEND.type);
            channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        return usersExpandMapper.queryMyFriends(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsg chatMsg) {
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setId(sid.nextShort());
        chatRecord.setSendUserId(chatMsg.getSenderId());
        chatRecord.setReceiveUserId(chatMsg.getReceiverId());
        chatRecord.setMessage(chatMsg.getMsg());
        chatRecord.setAudioUrl(chatMsg.getAudioUrl());
        chatRecord.setSignFlag(MsgSignFlag.UNSIGN.type);
        chatRecord.setCreateTime(new Date());
        chatRecordMapper.insert(chatRecord);
        return chatRecord.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        chatRecordMapper.batchUpdateMsgSigned(msgIdList);
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public List<ChatRecord> getUnReadMsgList(String receiveUsrId) {
        Example example = new Example(ChatRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("signFlag",MsgSignFlag.UNSIGN.type);
        criteria.andEqualTo("receiveUserId",receiveUsrId);
        return chatRecordMapper.selectByExample(example);
    }
}
