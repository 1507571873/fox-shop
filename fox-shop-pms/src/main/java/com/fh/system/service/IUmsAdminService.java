package com.fh.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fh.resource.entity.UmsResource;
import com.fh.role.entity.UmsRole;
import com.fh.system.entity.UmsAdmin;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-07
 */
public interface IUmsAdminService extends IService<UmsAdmin> {

    /**
     * 根据用户id查询他所拥有的资源
     * @param id
     * @return List<UmsResource>
     */
    List<UmsResource> queryResourceByAdmin(Long id,String userName);

    /**
     * 根据用户id查询他所拥有的角色
     * @param id
     * @return List<UmsRole>
     */
    List<UmsRole> queryRoleByAdmin(Long id,String userName);

    /**
     * 生成token值
     * @param userName
     * @param password
     * @return String
     */
    String login(String userName, String password);

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    UmsAdmin getUserByName(String userName);

    /**
     * 退出
     * @param token
     */
    void deleteToken(String token);
}

