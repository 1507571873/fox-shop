package com.fh.authorization;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fh.utils.CommonsReturn;
import com.fh.utils.LoginJwtUtils;
import com.fh.utils.ReturnCode;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginAuthorizationAop
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/6 21:57
 * @Version 1.0
 **/
/*@Aspect
@Component
@Order(4)*/
public class LoginAuthorizationAop {
    @Autowired
    private HttpServletRequest request;

    @Resource
    private LoginJwtUtils jwtUtils;

    private static final String PRE_TOKEN ="ACCESS_TOKEN";

    @Resource
    private RedisTemplate redisTemplate;
    @Around(value = "execution(* com.fh.*.controller..*.*(..)) ")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //获取token
        String token=request.getHeader("Authorization-token");
        //判断token是否为空 为空说明登录失效
        if (StringUtils.isBlank(token)){
            return  CommonsReturn.error(ReturnCode.LOGIN_DISADLED);

        }
        //解析校验token值
        CommonsReturn commonsReturn = jwtUtils.authToken(token);
        //判断返回的状态码是否为200
        if (commonsReturn.getCode()!=200){
            return commonsReturn;
        }
        //获取用户信息
        Claims claims = (Claims) commonsReturn.getData();
        String userId= String.valueOf(claims.get("userId"));
        String accessKey=PRE_TOKEN+userId+":"+token;
        if (!redisTemplate.hasKey(accessKey)){
            return CommonsReturn.error(ReturnCode.LOGIN_DISADLED);
        }
        Long currentTime=System.currentTimeMillis();
        //续签
        redisTemplate.opsForValue().set(accessKey,currentTime);
        redisTemplate.expire(accessKey,30, TimeUnit.MINUTES);
        
        Object obj = null;
        try {
            obj=joinPoint.proceed();
        }catch (Throwable throwable){
            throwable.printStackTrace();
            throw throwable;
        }
        return obj;
    }
}
