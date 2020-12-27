package com.fh.security.config;

import com.fh.resource.entity.UmsResource;

import com.fh.role.entity.UmsRole;
import com.fh.security.bo.AdminUserDetails;
import com.fh.security.filter.JwtAuthenticationTokenFilter;
import com.fh.security.filter.RestAuthenticationEntryPoint;
import com.fh.security.filter.RestfulAccessDeniedHandler;
import com.fh.system.entity.UmsAdmin;
import com.fh.system.service.IUmsAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * springSecurity配置
 *
 * @ClassName ShopSecurityConfig
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/20 12:16
 * @Version 1.0
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ShopSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private IUmsAdminService adminService;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    /**
     * 框架的过滤器
     * 配置那些类需要被拦截 那些不用被拦截
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
            · disable()禁用csrf
            · sessionManagement() 基于token 所以不需要session
            · sessionCreationPolicy(SessionCreationPolicy.STATELESS) 关闭session
            · authorizeRequests() 请求的认证设置
            · antMatchers().permitAll() 不用被拦截的请求
            · antMatchers(HttpMethod.OPTIONS).permitAll() 放开预检的options请求
            · anyRequest().authenticated() 拦截其他的请求
         */
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/LoginController/**","/UploadPhoto").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();
        //禁用缓存      后续用redis
        http.headers().cacheControl();
        //把自定义的拦截器加载过来
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    /**
     * 认证 ：权限认证，登录认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());

    }

    /**
     * 拦截器
     * @return
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
    /**
     * 根据用户名称 获取用户的信息和权限
     * @return
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return userName->{
            UmsAdmin admin = adminService.getUserByName(userName);
            //判断是否有这个用户
            if (admin!=null){
                List<UmsResource> resourceList=adminService.queryResourceByAdmin(admin.getId(),userName);
                List<UmsRole> roleList=adminService.queryRoleByAdmin(admin.getId(),userName);
                return new AdminUserDetails(admin,roleList,resourceList);
            }
            throw new UsernameNotFoundException("用户不存在");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
