package com.fh.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fh.system.entity.UmsAdmin;

import com.fh.system.service.IUmsAdminService;
import com.fh.utils.CommonsReturn;
import com.fh.utils.LoginJwtUtils;
import com.fh.utils.MD5Util;
import com.fh.utils.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/8 18:46
 * @Version 1.0
 **/
@RestController
@RequestMapping("/LoginController")
public class LoginController {
    @Resource
    private IUmsAdminService userService;

    @Resource
    private HttpServletRequest request;

    @PostMapping
    public CommonsReturn doLogin(@RequestParam("userName") String userName, @RequestParam("password") String password){
        //判断用户名 密码是否为空
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            return CommonsReturn.error(ReturnCode.USERNAME_PASSWORD_NULL);
        }
        String token = userService.login(userName,password);
        if (token==null){
            //用户名 或密码错误
            return CommonsReturn.error(ReturnCode.LOGIN_NAME_PASSWORD_ERROR);
        }
        /*//根据用户名查询用户信息
        UmsAdmin user = userService.getOne(new QueryWrapper<UmsAdmin>().eq("username", userName));
        //判断用户是否为空
        if (user==null){
            return  CommonsReturn.error(ReturnCode.USERNAME_NOEXIST);
        }
        //判断密码是否正确
        String passwordMd5 = new BCryptPasswordEncoder().encode(password);
        if (!passwordMd5.equals(user.getPassword())){
            return  CommonsReturn.error(ReturnCode.PASSWORD_ERROR);
        }
        //调用jwtUtil生成token 并传给前台
        String token = loginJwtUtils.crearToken(user);
        //获取当前时间戳
        Long currentTime=System.currentTimeMillis();
        String accessKey=pre_token+user.getId()+":"+token;
        redisTemplate.opsForValue().set(accessKey,currentTime);
        redisTemplate.expire(accessKey,30, TimeUnit.MINUTES);*/
        return CommonsReturn.success(token);
    }

    @GetMapping("/LoginOut")
    public CommonsReturn LoginOut(){
        //获取到token值
        String token = request.getHeader("Authorization-token");
        userService.deleteToken(token);

        return CommonsReturn.success();
    }
}
