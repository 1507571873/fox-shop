package com.fh.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.resource.entity.UmsResource;
import com.fh.role.entity.UmsRole;
import com.fh.system.entity.UmsAdmin;
import com.fh.system.mapper.UmsAdminMapper;
import com.fh.system.service.IUmsAdminService;
import com.fh.utils.SecurityJwtUtils;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-07
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements IUmsAdminService {

    @Resource
    private UmsAdminMapper adminMapper;

    @Resource
    private UserDetailsService detailsService;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SecurityJwtUtils securityJwtUtils;
    private static final String pre_token="ACCESS_TOKEN:";
    private static final String admin_login="LOGIN:";
    /**
     * 根据用户Id查询它拥有的资源
     * @param id
     * @return List<UmsResource>
     */
    @Override
    public List<UmsResource> queryResourceByAdmin(Long id,String userName) {
        //声明一个redis 的key
        String key=admin_login+userName+":RESOURCE";
        //判断这个key 是否存在
        if (redisTemplate.hasKey(key)){
            //获取key对应的value值 并返回
            return (List<UmsResource>) redisTemplate.opsForValue().get(key);
        }
        //如果key不存在 也就是说明redis中没有这个值  所以我们要从数据库中进行查询
        List<UmsResource> list = adminMapper.queryResourceByAdmin(id);
        //把查询出来的值放到redis中
        redisTemplate.opsForValue().set(key,list);
        //设置超时时间
        redisTemplate.expire(key,30, TimeUnit.MINUTES);

        return list;
    }

    /**
     * 根据用户Id查询它拥有的角色
     * @param id
     * @return List<UmsRole>
     */
    @Override
    public List<UmsRole> queryRoleByAdmin(Long id,String userName) {
        //声明一个redis 的key
        String key=admin_login+userName+":ROLE";
        //判断这个key 是否存在
        if (redisTemplate.hasKey(key)){
            //获取key对应的value值 并返回
            return (List<UmsRole>) redisTemplate.opsForValue().get(key);
        }
        //如果key不存在 也就是说明redis中没有这个值  所以我们要从数据库中进行查询
        List<UmsRole> list = adminMapper.queryRoleByAdmin(id);
        //把查询出来的值放到redis中
        redisTemplate.opsForValue().set(key,list);
        //设置超时时间
        redisTemplate.expire(key,30, TimeUnit.MINUTES);
        return list;
    }
    @Override
    public UmsAdmin getUserByName(String userName) {
        QueryWrapper<UmsAdmin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userName);
        String key = admin_login+userName+":USER";
        if (redisTemplate.hasKey(key)){
            return (UmsAdmin) redisTemplate.opsForValue().get(key);
        }
        UmsAdmin admin = getOne(queryWrapper);
        redisTemplate.opsForValue().set(key,admin);
        redisTemplate.expire(key,30,TimeUnit.MINUTES);
        return admin;
    }

    /**
     * 退出
     * @param token
     */
    @Override
    public void deleteToken(String token) {
        String userName = securityJwtUtils.getUserName(token);
        clearAdminRedis(userName);
    }

    /**
     *  登录
     * @param userName
     * @param password
     * @return String
     */
    @Override
    public String login(String userName, String password) {
        String token = null;
        try {
            //调用认证登录方法
            UserDetails userDetails = detailsService.loadUserByUsername(userName);
            //判断密码是否正确
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("用户密码不正确");
            }
            //清理上次登录的用户信息
            clearAdminRedis(userName);
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            //把authenticationToken放到spring上下文里 让下边的方法也可以获取到authenticationToken
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //生成token值 并设置过期时间
            token=securityJwtUtils.crearToken(userDetails);
            //获取当前时间戳
            Long currentTime=System.currentTimeMillis();
            String accessKey=pre_token+userDetails.getUsername()+":"+token;
            redisTemplate.opsForValue().set(accessKey,currentTime);
            redisTemplate.expire(accessKey,30, TimeUnit.MINUTES);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return token;
    }

    public void clearAdminRedis(String userName){
        //清理token信息
        String tokenKey = pre_token+userName+":*";
        Set<String> tokenkeys = redisTemplate.keys(tokenKey);
        if (!CollectionUtils.isEmpty(tokenkeys)){
            redisTemplate.delete(tokenkeys);
        }
        //清理上次登录的用户信息
        String adminKey=admin_login+userName+":*";
        Set<String> keys = redisTemplate.keys(adminKey);
        if (!CollectionUtils.isEmpty(keys)){
            redisTemplate.delete(keys);
        }
    }
}
