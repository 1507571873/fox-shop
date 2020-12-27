package com.fh.role.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.role.entity.UmsRole;
import com.fh.role.service.IUmsRoleService;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-16
 */
@RestController
@RequestMapping("/ums/role")
public class UmsRoleController {

    @Resource
    private IUmsRoleService roleService;

    /**
     * 查询 分页查询 条件查询
     * @param page
     * @param seachName
     * @return CommonsReturn
     */
    @GetMapping
    public CommonsReturn queryRoleData(Page<UmsRole> page,String keyword){
        QueryWrapper<UmsRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",keyword);
        IPage<UmsRole> list = roleService.page(page,queryWrapper);
        return CommonsReturn.success(list);
    }

    /**
     * 新增修改
     * @param umsRole
     * @return CommonsReturn
     */
    @PostMapping
    public CommonsReturn saveOrUpdate(UmsRole umsRole){
        if (umsRole.getId()==null){
            umsRole.setCreateTime(new Date());
        }
        roleService.saveOrUpdate(umsRole);
        return CommonsReturn.success();
    }

    /**
     * 根据唯一标识Id查询
     * @param id
     * @return CommonsReturn
     */
    @GetMapping("/ById")
    public CommonsReturn queryRoleById(Integer id){
        UmsRole role = roleService.getById(id);
        return CommonsReturn.success(role);
    }

    /**
     * 查询所有 用户赋角色的下拉框
     * @return CommonsReturn
     */
    @GetMapping("/All")
    public CommonsReturn queryRoleAll(){
        List<UmsRole> list = roleService.list();
        return CommonsReturn.success(list);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping
    public CommonsReturn deleteRoll(Integer id){
        roleService.removeById(id);
        return CommonsReturn.success();
    }
}
