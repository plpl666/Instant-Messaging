package com.imooc.service.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 用于检测channel的心跳handler 删除断开或未连接的channel
 * 继承ChannelInboundHandlerAdapter类不需要实现channelRead0
 */
public class WSHeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是否是IdleStateEvent(用于触发用户事件: 读空闲/写空闲/读写空闲)
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("进入读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("进入写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("channel关闭前,users的数量:" + WSChatHandler.clients.size());
                Channel channel = ctx.channel();
                //关闭不用的channel以防资源浪费
                channel.close();
                System.out.println("channel关闭后,users的数量:" + WSChatHandler.clients.size());
            }
        }
    }

}
