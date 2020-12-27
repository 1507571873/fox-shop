package com.fh.security.bo;

import com.fh.resource.entity.UmsResource;
import com.fh.role.entity.UmsRole;
import com.fh.system.entity.UmsAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName AdminUserDetails
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/20 19:16
 * @Version 1.0
 **/
public class AdminUserDetails implements UserDetails {


    private UmsAdmin umsAdmin;
    private List<UmsRole> roleList;
    private List<UmsResource> resourceList;

    /**
     * 有参构造
     * @param umsAdmin
     * @param roleList
     * @param resourceList
     */
    public AdminUserDetails(UmsAdmin umsAdmin,List<UmsRole> roleList,List<UmsResource> resourceList){
        this.resourceList=resourceList;
        this.roleList=roleList;
        this.umsAdmin=umsAdmin;
    }
    /**
     * 给用户授予角色和权限
     * 角色和权限公用一个GrantedAuthority集合 唯一的不同 就是角色要加前缀 ROLE_ security没有从属关系
     * @return list
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        resourceList.forEach(resource->{
            list.add(new SimpleGrantedAuthority(resource.getKeyword()));
        });
        roleList.forEach(role->{
            list.add(new SimpleGrantedAuthority("ROLE_"+role.getKeyword()));
        });
        return list;
    }

    /**
     *  获取用户密码
     * @return password
     */
    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    /**
     *  获取用户名
     * @return username
     */
    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    /**
     *  是否超时
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *  账户是否锁定
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 判断用户是否启用
     *  1——》是 0——》否
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus().equals(1);
    }
}
