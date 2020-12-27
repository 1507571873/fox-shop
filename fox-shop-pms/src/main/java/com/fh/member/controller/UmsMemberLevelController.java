package com.fh.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.member.entity.UmsMemberLevel;
import com.fh.member.service.IUmsMemberLevelService;
import com.fh.utils.CommonsReturn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 会员等级表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-13
 */
@RestController
@RequestMapping("/pms/member")
public class UmsMemberLevelController {

    @Resource
    private IUmsMemberLevelService memberLevelService;

    @GetMapping("/{status}")
    public CommonsReturn queryMemberLevelGetStatus(@PathVariable("status") Integer status){
        QueryWrapper<UmsMemberLevel> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("default_status",status);
        List<UmsMemberLevel> list = memberLevelService.list(queryWrapper);
        return CommonsReturn.success(list);
    }

}
