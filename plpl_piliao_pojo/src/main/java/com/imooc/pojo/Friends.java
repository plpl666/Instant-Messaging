package com.imooc.pojo;

import javax.persistence.*;

public class Friends {
    /**
     * 编号
     */
    @Id
    private String id;

    /**
     * 用户编号
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 好友编号
     */
    @Column(name = "friend_id")
    private String friendId;

    /**
     * 获取编号
     *
     * @return id - 编号
     */
    public String getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户编号
     *
     * @return user_id - 用户编号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户编号
     *
     * @param userId 用户编号
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取好友编号
     *
     * @return friend_id - 好友编号
     */
    public String getFriendId() {
        return friendId;
    }

    /**
     * 设置好友编号
     *
     * @param friendId 好友编号
     */
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}