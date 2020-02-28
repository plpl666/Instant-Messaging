package com.imooc.service.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class WSServer {

    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    private WSServer() {
        parentGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup,childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }

    private static class Singleton {
        private final static WSServer INSTANCE = new WSServer();
    }

    public static WSServer getInstance() {
        return Singleton.INSTANCE;
    }

    public void start() {
        this.channelFuture = serverBootstrap.bind(8088);
        System.err.println("netty webSocket 启动完毕!");
    }

//    public static void main(String[] args) throws InterruptedException {
//        EventLoopGroup parentGroup = new NioEventLoopGroup();
//        EventLoopGroup childGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(parentGroup,childGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new WSServerInitializer());
//            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
//            channelFuture.channel().closeFuture().sync();
//        } finally {
//            parentGroup.shutdownGracefully();
//            childGroup.shutdownGracefully();
//        }
//    }

}
