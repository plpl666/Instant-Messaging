package com.imooc.service.websocket;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户的编号和channel的关联关系处理
 */
public class UserChannelRel {

    private static Map<String, Channel> manager = new HashMap<>();

    /**
     * 用户编号和channel关联
     * @param senderId 用户编号
     * @param channel channel
     */
    public static void put(String senderId, Channel channel) {
        manager.put(senderId,channel);
    }

    /**
     * 通过用户编号返回所对应的channel
     * @param senderId 用户编号
     * @return channel对象
     */
    public static Channel getChannel(String senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        for (HashMap.Entry<String,Channel> entry : manager.entrySet()) {
            System.out.println("UserId:" + entry.getKey() + ", ChannelId:" + entry.getValue().id().asLongText());
        }
    }

}
