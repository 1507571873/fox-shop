package com.fh.utils;



import com.fh.system.entity.UmsAdmin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginJwtUtils
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/6 21:02
 * @Version 1.0
 **/
@Component

public class LoginJwtUtils {

    @Value("${user.jwtset.signature}")
    private String signature;
    @Value("${user.jwtset.exptime}")
    private Long exptime;
    @Value("${user.jwtset.alg}")
    private String alg;
    @Value("${user.jwtset.type}")
    private String type;

    public String crearToken(UmsAdmin user){
//JWt token 由三部分组成
        //1.头部信息
        Map<String,Object> headerMap = new HashMap<>();
        headerMap.put("alg",alg);
        headerMap.put("type",type);

        //有效负载    主要放一些用户信息
        Map<String,Object> payLoadMap = new HashMap<>();
        payLoadMap.put("userId",user.getId());
        payLoadMap.put("realName",user.getUsername());

        //设置失效时间
        long startTime = System.currentTimeMillis();
        long expTime = startTime+exptime;

        //生成JWT
        String token = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(payLoadMap)
                .setExpiration(new Date(expTime))
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        System.out.println(token);
        return token;
    }

    public CommonsReturn authToken(String token){
        try {
            Claims Tang = Jwts.parser()
                    .setSigningKey(signature)
                    .parseClaimsJws(token)
                    .getBody();
            return CommonsReturn.success(Tang);

        }catch (Exception e){
            return CommonsReturn.error(ReturnCode.LOGIN_DISADLED);
        }

    }
}
