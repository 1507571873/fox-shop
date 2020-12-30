package com.fh.websocket;

import com.fh.system.entity.UmsAdmin;
import com.fh.utils.SecurityJwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName WebSocketServer
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/30 16:44
 * @Version 1.0
 **/
@Component
@ServerEndpoint(value="/websocket",configurator = WebSocketConfig.class)
public class WebSocketServer {
    private static final String admin_login="LOGIN:";
    private static AtomicInteger online = new AtomicInteger();
    private static SecurityJwtUtils securityJwtUtils;
    private static RedisTemplate redisTemplate;
    private static final Map<String, Session> sessionPools=new HashMap<>();

    /**
     * 判断用户是否在别的设备登录
     * @param userName
     */
    public void checkCurrUserLogin(String userName) throws IOException {
        Session session = sessionPools.get(userName);
        if (session!=null){
            session.getBasicRemote().sendText("异地登陆提醒：您的账户"+userName+"正在异地登陆，请确认是否本人登录");
        }
    }
    @Resource
    public void setSecurityJwtUtils(SecurityJwtUtils securityJwtUtils){
        this.securityJwtUtils=securityJwtUtils;
    }
    @Resource
    public void setRedisTemplate(RedisTemplate redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    /**
     * 连接建立成功调用
     * @param session 客户端与socket建立的会话
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        String token = (String) session.getUserProperties().get("token");
        String userName = securityJwtUtils.getUserName(token);
        String userKey = admin_login+userName+":USER";
        UmsAdmin admin = (UmsAdmin) redisTemplate.opsForValue().get(userKey);
        sessionPools.put(userName,session);
        for (Session s:sessionPools.values()){
            //不提示自己上线
            if (!session.equals(s)){
                s.getBasicRemote().sendText("这个人他上线了："+admin.getNickName());
            }
        }
    }
    /**
     * 关闭连接时调用
     * @param  session
     */
    @OnClose
    public void onClose(Session session){
        String token = (String) session.getUserProperties().get("token");
        String userName = securityJwtUtils.getUserName(token);
        System.out.println(userName+"已经下线");
        online.decrementAndGet();
        sessionPools.remove(userName);
    }
    /**
     * 收到客户端消息时触发（群发）
     * @param message
     * @throws
     */
    @OnMessage
    public void onMessage(Session session,String message) throws IOException {
        for (Session s:sessionPools.values()){
            s.getBasicRemote().sendText("当前在线人数"+sessionPools.values().size());
        }
    }
    /**
     * 发生错误时候
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session,Throwable throwable){

    }
}
