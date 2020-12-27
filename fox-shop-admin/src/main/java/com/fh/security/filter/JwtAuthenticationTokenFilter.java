package com.fh.security.filter;

import com.fh.utils.CommonsReturn;
import com.fh.utils.SecurityJwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * jwt登录授权过滤器
 * @ClassName JwtAuthenticationTokenFilter
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/20 21:22
 * @Version 1.0
 **/
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private SecurityJwtUtils securityJwtUtils;
    @Resource
    private UserDetailsService userDetailsService;
    private static final String PRE_TOKEN ="ACCESS_TOKEN:";

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取到token值
        String token = request.getHeader("Authorization-token");
        //判断token值是否为空
        if (StringUtils.isNotBlank(token)){
            CommonsReturn commonsReturn = securityJwtUtils.authToken(token);
            if (commonsReturn.getCode()==200 && SecurityContextHolder.getContext().getAuthentication()==null){
                //获取用户名称
                String userName = securityJwtUtils.getUserName(token);
                UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
                String accessKey=PRE_TOKEN+userDetails.getUsername()+":"+token;

                if (redisTemplate.hasKey(accessKey)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    Long currentTime=System.currentTimeMillis();
                    //续签
                    redisTemplate.opsForValue().set(accessKey,currentTime);
                    redisTemplate.expire(accessKey,30, TimeUnit.MINUTES);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}