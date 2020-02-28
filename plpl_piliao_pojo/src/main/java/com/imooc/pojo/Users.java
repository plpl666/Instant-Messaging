package com.imooc.pojo;

import javax.persistence.*;

public class Users {
    /**
     * 用户编号
     */
    @Id
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像路径
     */
    @Column(name = "face_img")
    private String faceImg;

    /**
     * 大头像路径
     */
    @Column(name = "face_img_big")
    private String faceImgBig;

    /**
     * 二维码
     */
    private String qrcode;

    /**
     * 手机编号
     */
    private String cid;

    /**
     * 获取用户编号
     *
     * @return id - 用户编号
     */
    public String getId() {
        return id;
    }

    /**
     * 设置用户编号
     *
     * @param id 用户编号
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取头像路径
     *
     * @return face_img - 头像路径
     */
    public String getFaceImg() {
        return faceImg;
    }

    /**
     * 设置头像路径
     *
     * @param faceImg 头像路径
     */
    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    /**
     * 获取大头像路径
     *
     * @return face_img_big - 大头像路径
     */
    public String getFaceImgBig() {
        return faceImgBig;
    }

    /**
     * 设置大头像路径
     *
     * @param faceImgBig 大头像路径
     */
    public void setFaceImgBig(String faceImgBig) {
        this.faceImgBig = faceImgBig;
    }

    /**
     * 获取二维码
     *
     * @return qrcode - 二维码
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * 设置二维码
     *
     * @param qrcode 二维码
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * 获取手机编号
     *
     * @return cid - 手机编号
     */
    public String getCid() {
        return cid;
    }

    /**
     * 设置手机编号
     *
     * @param cid 手机编号
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", faceImg='" + faceImg + '\'' +
                ", faceImgBig='" + faceImgBig + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", cid='" + cid + '\'' +
                '}';
    }

}