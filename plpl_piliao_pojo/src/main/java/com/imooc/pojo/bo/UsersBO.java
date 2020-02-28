package com.imooc.pojo.bo;

public class UsersBO {

    private String userId;
    private String faceData;
    private String nickname;
    private String faceImg;
    private String faceImgBig;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFaceData() {
        return faceData;
    }

    public void setFaceData(String faceData) {
        this.faceData = faceData;
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
}