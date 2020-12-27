package com.fh.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.resource.entity.UmsResource;
import com.fh.role.entity.UmsRole;
import com.fh.system.entity.UmsAdmin;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-07
 */
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {
    /**
     * 根据用户id查询他所拥有的资源
     * @param id
     * @return
     */
    List<UmsResource> queryResourceByAdmin(Long id);

    /**
     * 根据用户id查询他所拥有的角色
     * @param id
     * @return
     */
    List<UmsRole> queryRoleByAdmin(Long id);
}
