package com.imooc.pojo.vo;

public class UsersVO {


    private String id;
    private String username;
    private String nickname;
    private String faceImg;
    private String faceImgBig;
    private String qrcode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFaceImg() {
        return faceImg;
    }

    public void setFaceImg(String faceImg) {
        this.faceImg = faceImg;
    }

    public String getFaceImgBig() {
        return faceImgBig;
    }

    public void setFaceImgBig(String faceImgBig) {
        this.faceImgBig = faceImgBig;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}