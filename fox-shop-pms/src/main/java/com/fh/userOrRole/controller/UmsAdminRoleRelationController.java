package com.fh.userOrRole.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.config.LogsAnnotation;
import com.fh.userOrRole.entity.UmsAdminRoleBo;
import com.fh.userOrRole.entity.UmsAdminRoleRelation;
import com.fh.userOrRole.service.IUmsAdminRoleRelationService;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 后台用户和角色关系表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-16
 */
@RestController
@RequestMapping("/ums/userOrRole")
public class UmsAdminRoleRelationController {

    @Resource
    private IUmsAdminRoleRelationService adminRoleRelationService;

    /**
     * 用户赋角色保存
     * @param umsAdminRoleBo
     * @return
     */
    @PostMapping
    @LogsAnnotation("后台用户和角色关系表 用户赋角色保存")
    public CommonsReturn saveUserOrRole(@RequestBody UmsAdminRoleBo umsAdminRoleBo){
        QueryWrapper<UmsAdminRoleRelation> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("admin_id",umsAdminRoleBo.getAdminId());
        adminRoleRelationService.remove(queryWrapper);
        adminRoleRelationService.saveBatch(umsAdminRoleBo.getUmsAdminRoleList());
        return CommonsReturn.success();
    }

    /**
     * 用户赋角色回显
     * @param adminId
     * @return
     */
    @GetMapping("/ById")
    @LogsAnnotation("后台用户和角色关系表 用户赋角色回显")
    public CommonsReturn queryUserOrRole(Integer adminId){
        QueryWrapper<UmsAdminRoleRelation> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("admin_id",adminId);
        List<UmsAdminRoleRelation> list = adminRoleRelationService.list(queryWrapper);
        return CommonsReturn.success(list);
    }
}
