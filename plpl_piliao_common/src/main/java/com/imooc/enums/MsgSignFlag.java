package com.imooc.enums;

public enum MsgSignFlag {

    UNSIGN(0,"未签收"),
    SIGNED(1,"已签收");

    public final Integer type;
    public final String content;

    MsgSignFlag(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}
