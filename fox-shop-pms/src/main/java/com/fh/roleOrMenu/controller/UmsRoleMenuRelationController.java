package com.fh.roleOrMenu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.config.LogsAnnotation;
import com.fh.roleOrMenu.entity.RoleMenuRelationBo;
import com.fh.roleOrMenu.entity.UmsRoleMenuRelation;
import com.fh.roleOrMenu.service.IUmsRoleMenuRelationService;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台角色菜单关系表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/roleOrMenu")
public class UmsRoleMenuRelationController {


    @Resource
    private IUmsRoleMenuRelationService roleMenuRelationService;

    @GetMapping
    public CommonsReturn queryRoleMenuRelation(Integer roleId){
        QueryWrapper<UmsRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        List<UmsRoleMenuRelation> list = roleMenuRelationService.list(queryWrapper);
        return CommonsReturn.success(list);
    }

    /**
     * 保存
     * @param roleMenuRelationBo
     * @return
     */
    @PostMapping
    @LogsAnnotation("后台角色菜单关系表 保存")
    public CommonsReturn saveRoleMenuRelation(@RequestBody RoleMenuRelationBo roleMenuRelationBo){
        roleMenuRelationService.remove( new QueryWrapper<UmsRoleMenuRelation>().eq("role_id",roleMenuRelationBo.getRoleId()));
        roleMenuRelationService.saveBatch(roleMenuRelationBo.getRoleOrMenuList());
        return CommonsReturn.success();
    }
}
