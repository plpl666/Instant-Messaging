package com.imooc.service.websocket;

import com.imooc.enums.MsgAction;
import com.imooc.fastDFS.FileUtils;
import com.imooc.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.SpringUtil;
import com.imooc.service.websocket.pojo.DataContent;
import com.imooc.service.websocket.pojo.ChatMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


//TextWebSocketFrame:在netty中,是用于为webSocket专门处理文本对象,frame是消息载体
public class WSChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用于记录和管理所有客户端的channel
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        //获取客户端传输过来的消息
        String content = textWebSocketFrame.text();
        DataContent dataContent = JsonUtils.jsonToPojo(content,DataContent.class);
        //判断消息类型,根据类型处理不同业务
        assert dataContent != null;
        Integer action = dataContent.getAction();
        if (action.equals(MsgAction.CONNECT.type)) {
            // 1)当webSocket第一次连接的时候,初始化channel,并把channel和userId关联起来
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(senderId,channelHandlerContext.channel());
            //测试
            for (Channel c : clients) {
                System.out.println(c.id().asLongText());
            }
            UserChannelRel.output();
        } else if (action.equals(MsgAction.CHAT.type)) {
            // 2)聊天类型的消息,把聊天记录保存到数据库,同时标记消息的签收状态[未签收]
            ChatMsg chatMsg = dataContent.getChatMsg();
            String receiverId = chatMsg.getReceiverId();
            UserService userService = (UserService) SpringUtil.getBean("userService");
            String msgId = userService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);
            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);
            //发送消息给接收者
            //获取接收者的channel
            Channel receiverChannel = UserChannelRel.getChannel(receiverId);
            if (receiverChannel == null) {
                //TODO receiverChannel为空代表用户离线,推送消息
            } else {
                //当receiverChannel不为空,需要从ChannelGroup中查找对应channel是否存在
                if (clients.find(receiverChannel.id()) != null) {
                    //用户在线,发送消息给用户
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContentMsg)));
                } else {
                    //TODO 用户离线推送消息
                }
            }
        } else if (action.equals(MsgAction.SIGNED.type)) {
            // 3)签收消息类型,针对具体消息进行签收,修改数据库中对应的签收状态[已签收]
            UserService userService = (UserService) SpringUtil.getBean("userService");
            //扩展字段在signed类型的消息中代表所要签收的消息编号(可以是多个),逗号间隔
            String msgIds = dataContent.getExpand();
            String msgIdArr[] = msgIds.split(",");
            List<String> msgIdList = new ArrayList<>();
            for (String msgId : msgIdArr) {
                if (StringUtils.isNotBlank(msgId)) {
                    msgIdList.add(msgId);
                }
            }
            if (!msgIdList.isEmpty()) {
                //批量签收
                userService.updateMsgSigned(msgIdList);
            }
        } else if (action.equals(MsgAction.KEEPALIVE.type)) {
            // 4)心跳类型的消息
            System.out.println("收到来自channel为:" + channelHandlerContext.channel() + "的channel包");
        }

    }

    /*
      当客户端连接服务器之后(打开连接)
      获取客户端channel,并保存到ChannelGroup中去管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved时,ChannelGroup会自动移除对应客户端的channel
        clients.remove(ctx.channel());
        System.out.println("客户端断开,channel对应的长ID:" + ctx.channel().id().asLongText());
        System.out.println("客户端断开,channel对应的短ID:" + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常后关闭channel,随后从ChannelGroup中移除channel
        ctx.channel().closeFuture().sync();
        clients.remove(ctx.channel());
    }
}
