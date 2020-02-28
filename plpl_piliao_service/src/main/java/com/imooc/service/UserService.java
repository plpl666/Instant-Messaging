package com.imooc.service;

import com.imooc.pojo.ChatRecord;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.FriendRequestVO;
import com.imooc.pojo.vo.MyFriendsVO;
import com.imooc.service.websocket.pojo.ChatMsg;

import java.io.IOException;
import java.util.List;

public interface UserService {

    /**
     * 判断用户是否存在
     * @param user 用户对象
     * @return 用户对象
     */
    Users userIsExist(Users user);

    /**
     * 添加用户
     * @param password 用户密码
     * @param cid 用户手机唯一标识
     * @return 生成的用户账号
     */
    String addUser(String password, String cid) throws IOException;

    /**
     * 修改用户属性 用户上传头像,更改昵称
     * @param users 用户对象
     */
    void updateUser(Users users);

    /**
     * 通过编号查询用户
     * @param id 用户编号
     * @return 用户对象
     */
    Users getUser(String id);

    /**
     * 搜索是否满足添加条件
     * @param myUserId 用户编号
     * @param friendUsername 被搜索用户账号
     * @return status 0 1 2 3
     */
    Integer preconditionSearchFriends(String myUserId, String friendUsername);

    /**
     * 根据用户账号查询用户对象
     * @param username 用户账号
     * @return 用户对象
     */
    Users queryUserByUsername(String username);

    /**
     * 用户发送添加好友请求
     * @param myUserId 用户编号
     * @param friendId 好友编号
     */
    void sendFriendRequest(String myUserId, String friendId);

    /**
     * 获取用户好友请求列表
     * @param id 用户编号
     * @return 用户对象集合
     */
    List<FriendRequestVO> queryFriendRequestList(String id);

    /**
     * 删除用户好友请求
     * @param sendUserId 发送用户的编号
     * @param receiveUserId 接收用户的编号
     */
    void deleteFriendRequest(String sendUserId, String receiveUserId);

    /**
     * 通过好友请求 1. 保存好友 2. 逆向保存好友
     * @param sendUserId 发送用户的编号
     * @param receiveUserId 接收用户的编号
     */
    void saveFriendRequest(String sendUserId, String receiveUserId);

    /**
     * 查询用户好友
     * @param userId 用户编号
     */
    List<MyFriendsVO> queryMyFriends(String userId);

    /**
     * 保存用户发送的消息
     * @param chatMsg 消息对象
     * @return 消息编号
     */
    String saveMsg(ChatMsg chatMsg);

    /**
     * 批量签收消息
     * @param msgIdList 消息编号集合
     */
    void updateMsgSigned(List<String> msgIdList);

    /**
     * 获取未签收消息列表
     * @param receiveUsrId 接收用户编号
     * @return 消息对象集合
     */
    List<ChatRecord> getUnReadMsgList(String receiveUsrId);

}
