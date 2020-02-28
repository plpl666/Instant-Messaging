package com.imooc.plpl_piliao_api;

import com.imooc.service.websocket.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBoot implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            WSServer.getInstance().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
