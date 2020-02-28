package com.imooc.enums;

public enum  OperateFriendsRequestType {

    PASS(1,"通过"),
    IGNORE(0,"忽略");

    public final Integer type;
    public final String msg;

    OperateFriendsRequestType(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static String getMsgByType(Integer type) {
        switch (type) {
            case 0 :
                return OperateFriendsRequestType.IGNORE.msg;
            case 1 :
                return OperateFriendsRequestType.PASS.msg;
            default:
                return null;
        }
    }

}
