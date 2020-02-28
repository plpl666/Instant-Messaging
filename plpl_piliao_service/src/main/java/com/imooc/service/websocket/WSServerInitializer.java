package com.imooc.service.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;


public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //webSocket基于http协议,所以要有http编解码器
        pipeline.addLast(new HttpServerCodec());
        //对写入大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMassage进行聚合,聚合成FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        // ========================= 以上是用于支持http协议 ==========================

        // ========================= 增加心跳支持 start =============================
        //针对客户端,如果在1分钟时间中没有向服务器发送读写心跳(ALL),则主动断开
        //如果是读空闲或者写空闲则不去处理
        pipeline.addLast(new IdleStateHandler(20,40,60));
        //自定义的空闲事件检测
        pipeline.addLast(new WSHeartBeatHandler());
        // ========================= 增加心跳支持 end =============================

        // ========================= 以下是用于支持httpWebSocket =====================
        /*
          webSocket服务器处理的协议,用于指定给客户端访问的路由:/ws
          本handler会帮你处理一些繁重的复杂的事
          会帮助你处理握手动作
          对应webSocket来说,都是以frames进行传输的,不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //自定义handler
        pipeline.addLast(new WSChatHandler());

    }

}
