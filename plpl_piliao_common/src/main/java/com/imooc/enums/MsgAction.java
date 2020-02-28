package com.imooc.enums;

/**
 * 发送消息的动作(类型)
 */
public enum MsgAction {

    CONNECT(1,"第一次初始化连接(或重连)"),
    CHAT(2,"聊天消息"),
    SIGNED(3,"消息签收"),
    KEEPALIVE(4,"客户端保持心跳"),
    AGREE_FRIEND(5,"同意添加好友"),
    REQUEST_FRIEND(6,"加好友请求");

    public Integer type;
    public String content;

    MsgAction(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}
