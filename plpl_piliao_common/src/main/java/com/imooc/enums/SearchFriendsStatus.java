package com.imooc.enums;

public enum SearchFriendsStatus {

    SUCCESS(0,"OK"),
    USER_NOT_EXIST(1,"无此用户"),
    NOT_YOURSELF(2,"不能添加你自己"),
    ALREADY_FRIENDS(3,"该用户已经是你友...");

    public final Integer status;
    public final String msg;

    SearchFriendsStatus(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static String getMsgByKey(Integer status) {
        switch (status) {
            case 0 :
                return SUCCESS.msg;
            case 1 :
                return USER_NOT_EXIST.msg;
            case 2 :
                return NOT_YOURSELF.msg;
            case 3 :
                return ALREADY_FRIENDS.msg;
            default:
                return null;
        }
    }
}
