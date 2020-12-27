package com.fh.roleOrResource.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.roleOrResource.entity.UmsRoleResourceRelation;
import com.fh.roleOrResource.service.IUmsRoleResourceRelationService;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 后台角色资源关系表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/roleOrResource")
public class UmsRoleResourceRelationController {

    @Resource
    private IUmsRoleResourceRelationService roleResourceRelationService;


    @GetMapping("/ById")
    public CommonsReturn queryRoleResourceById(Integer roleId){
        QueryWrapper<UmsRoleResourceRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        List<UmsRoleResourceRelation> list = roleResourceRelationService.list(queryWrapper);
        return CommonsReturn.success(list);
    }

    @PostMapping
    public CommonsReturn saveRoleResource(@RequestParam Long roleId,@RequestParam List<Long> resourceIds){
        QueryWrapper<UmsRoleResourceRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        roleResourceRelationService.remove(queryWrapper);
        List<UmsRoleResourceRelation> list=new ArrayList<>();
        for (int i = 0; i < resourceIds.size(); i++) {
            UmsRoleResourceRelation umsRoleResourceRelation=new UmsRoleResourceRelation();
            umsRoleResourceRelation.setRoleId(roleId);
            umsRoleResourceRelation.setResourceId(resourceIds.get(i));
            list.add(umsRoleResourceRelation);
        }
        roleResourceRelationService.saveBatch(list);
        return CommonsReturn.success();
    }
}
