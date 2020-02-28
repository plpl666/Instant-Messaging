package com.imooc.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.imooc.enums.OperateFriendsRequestType;
import com.imooc.enums.SearchFriendsStatus;
import com.imooc.fastDFS.FastDFSClient;
import com.imooc.fastDFS.FileUtils;
import com.imooc.pojo.ChatRecord;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UsersBO;
import com.imooc.pojo.vo.FriendRequestVO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private FastDFSClient dfsClient;

    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("账号或密码不能为空");
        }
        user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
        Users userResult = userService.userIsExist(user);
        if (userResult != null) {
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(userResult,usersVO);
            return IMoocJSONResult.ok(usersVO);
        } else {
            return IMoocJSONResult.errorMsg("账号或密码不正确");
        }
    }

    @PostMapping("/register")
    public IMoocJSONResult register(@RequestBody Users user) throws Exception {
        if (StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("密码不能为空");
        }
        String username = userService.addUser(MD5Utils.getMD5Str(user.getPassword()),user.getCid());
        if (StringUtils.isBlank(username)) {
            return IMoocJSONResult.errorMsg("账号字典已为空,无法注册用户!");
        }
        return IMoocJSONResult.ok(username);
//        System.out.println(user.toString());
//        return IMoocJSONResult.ok("31242");
    }

    @PostMapping("/logout")
    public IMoocJSONResult logout(@RequestParam("id")String id) {
        if (StringUtils.isBlank(id)) {
            return IMoocJSONResult.errorMsg("error");
        }
        return IMoocJSONResult.ok();
    }

    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(@RequestBody UsersBO usersBO) throws Exception {
        //获取前端传来的base64字符串,然后转为文件对象再上传
        String base64Data = usersBO.getFaceData();
        String userFacePath = "D:\\_plpl_piliao\\" + usersBO.getUserId() + "\\face\\" + "userFace64.png";
        FileUtils.base64ToFile(userFacePath,base64Data);
        MultipartFile file = FileUtils.fileToMultipart(userFacePath);
        //上传文件到dfs
        String url = dfsClient.uploadBase64(file);
        //获取缩略图的url
        String thump = "_80x80";
        String arr[] = url.split("\\.");
        String thumbImgUrl = arr[0] + thump + "." + arr[1];
        //更新用户头像
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setFaceImg(thumbImgUrl);
        user.setFaceImgBig(url);
        userService.updateUser(user);
        //删除原来的头像
        if (StringUtils.isNotBlank(usersBO.getFaceImg()) && StringUtils.isNotBlank(usersBO.getFaceImgBig())) {
            dfsClient.deleteFile(usersBO.getFaceImgBig());
            dfsClient.deleteFile(usersBO.getFaceImg());
        }
        Users users = userService.getUser(user.getId());
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        return IMoocJSONResult.ok(usersVO);
    }

    @PostMapping("/updateNickname")
    public IMoocJSONResult updateNickname(@RequestBody UsersBO usersBO) {

        if (StringUtils.isBlank(usersBO.getNickname())) {
            return IMoocJSONResult.errorMsg("昵称不能为空!");
        }
        //更新昵称
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setNickname(usersBO.getNickname().trim());
        userService.updateUser(user);
        Users users = userService.getUser(user.getId());
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        return IMoocJSONResult.ok(usersVO);
    }

    @PostMapping("/searchFriends")
    public IMoocJSONResult searchFriends(String myUserId,String friendUsername) {
        if (StringUtils.isBlank(myUserId)||StringUtils.isBlank(friendUsername)) {
            return IMoocJSONResult.errorMsg("");
        }
        //前置条件 1 搜索的用户如果不存在,返回[无此用户]
        //前置条件 2 搜索的用户是自己,返回[不能添加自己]
        //前置条件 3 搜索的用户已经是好友,返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(myUserId,friendUsername);
        if (status.equals(SearchFriendsStatus.SUCCESS.status)) {
            Users users = userService.queryUserByUsername(friendUsername);
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(users,usersVO);
            return IMoocJSONResult.ok(usersVO);
        } else {
            return IMoocJSONResult.errorMsg(SearchFriendsStatus.getMsgByKey(status));
        }
    }

    @PostMapping("/saveFriends")
    public IMoocJSONResult saveFriends(String myUserId,String friendUsername) {
        if (StringUtils.isBlank(myUserId)||StringUtils.isBlank(friendUsername)) {
            return IMoocJSONResult.errorMsg("");
        }
        //前置条件 1 搜索的用户如果不存在,返回[无此用户]
        //前置条件 2 搜索的用户是自己,返回[不能添加自己]
        //前置条件 3 搜索的用户已经是好友,返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(myUserId,friendUsername);
        if (status.equals(SearchFriendsStatus.SUCCESS.status)) {
            Users users = userService.queryUserByUsername(friendUsername);
            userService.sendFriendRequest(myUserId,users.getId());
            return IMoocJSONResult.ok("请求已发送!");
        } else {
            return IMoocJSONResult.errorMsg(SearchFriendsStatus.getMsgByKey(status));
        }
    }

    @PostMapping("/getFriendsRequest")
    public IMoocJSONResult getFriendsRequest(String userId) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户编号不能为空!");
        }
        return IMoocJSONResult.ok(userService.queryFriendRequestList(userId));
    }

    @PostMapping("/operFriendsRequest")
    public IMoocJSONResult operFriendsRequest(String receiveUserId,String sendUserId,Integer operType) {
        if (!StringUtils.isNoneBlank(receiveUserId,sendUserId) || operType == null) {
            return IMoocJSONResult.errorMsg("");
        }
        if (StringUtils.isBlank(OperateFriendsRequestType.getMsgByType(operType))) {
            return IMoocJSONResult.errorMsg("");
        }

        if (operType.equals(OperateFriendsRequestType.IGNORE.type)) {
            //忽略好友请求,删除好友请求记录
            userService.deleteFriendRequest(sendUserId, receiveUserId);
        } else if (operType.equals(OperateFriendsRequestType.PASS.type)) {
            //通过好友请求,互相添加好友记录,并删除好友请求记录
            userService.saveFriendRequest(sendUserId, receiveUserId);
            userService.deleteFriendRequest(sendUserId, receiveUserId);
        }
        // 数据库查询好友列表
        return IMoocJSONResult.ok(userService.queryMyFriends(receiveUserId));
    }

    @PostMapping("/myFriendsList")
    public IMoocJSONResult myFriendsList(String userId) {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("");
        }
        // 数据库查询好友列表
        return IMoocJSONResult.ok(userService.queryMyFriends(userId));
    }

    //获取未签收的消息
    @PostMapping("/getUnReadMsgList")
    public IMoocJSONResult getUnReadMsgList(String receiveUserId) {
        if (StringUtils.isBlank(receiveUserId)) {
            return IMoocJSONResult.errorMsg("");
        }
        //查询未签收的消息
        List<ChatRecord> chatRecords = userService.getUnReadMsgList(receiveUserId);
        return IMoocJSONResult.ok(chatRecords);
    }

    @PostMapping("/sendAudio")
    public IMoocJSONResult sendAudio(MultipartFile audio) throws IOException {
        if (audio == null) {
            return IMoocJSONResult.errorMsg("");
        }
        String audioUrl = dfsClient.uploadFile(audio);
        return IMoocJSONResult.ok(audioUrl);
    }

}
