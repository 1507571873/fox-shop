package com.fh.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @ClassName WebSocketConfig
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/30 16:42
 * @Version 1.0
 **/
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        String token = request.getHeaders().get("sec-websocket-protocol").get(0);
        sec.getUserProperties().put("token",token);
        response.getHeaders().put("sec-websocket-protocol",request.getHeaders().get("sec-websocket-protocol"));
        super.modifyHandshake(sec, request, response);
    }

}
