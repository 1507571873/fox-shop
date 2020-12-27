package com.fh.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.system.entity.UmsAdmin;
import com.fh.system.service.IUmsAdminService;
import com.fh.utils.CommonsReturn;
import com.fh.utils.MD5Util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-07
 */
@RestController
@RequestMapping("/ums/admin")
public class UmsAdminController {

    @Resource
    private IUmsAdminService adminService;

    /**
     * 查询 分页查询 条件查询
     * @param page
     * @return
     */
    @GetMapping
    public CommonsReturn queryAdminData(Page<UmsAdmin> page,String keyword){
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<UmsAdmin>();
        queryWrapper.like("username",keyword).or().like("nick_name",keyword);
        IPage<UmsAdmin> list = adminService.page(page, queryWrapper);
        return CommonsReturn.success(list);
    }

    /**
     * 新增or修改
     * @param umsAdmin
     * @return
     */
    @PostMapping
    public CommonsReturn saveOrUpdate(UmsAdmin umsAdmin){
        if (umsAdmin.getId()==null){
            umsAdmin.setPassword(new BCryptPasswordEncoder().encode("123456"));
            umsAdmin.setCreateTime(new Date());
        }
        adminService.saveOrUpdate(umsAdmin);
        return CommonsReturn.success();
    }

    /**
     * 回显根据Id查询
     * @param id
     * @return
     */
    @GetMapping("/ById")
    public CommonsReturn queryAdminById(Integer id){
        UmsAdmin admin = adminService.getById(id);
        return CommonsReturn.success(admin);
    }


}
