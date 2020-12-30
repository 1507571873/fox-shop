package com.fh.resource.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.config.LogsAnnotation;
import com.fh.resource.entity.ResourceBo;
import com.fh.resource.entity.UmsResource;
import com.fh.resource.service.IUmsResourceService;
import com.fh.utils.CommonsReturn;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/resource")
public class UmsResourceController {

    @Resource
    private IUmsResourceService resourceService;

    /**
     * 查询 分页查询 条件查询
     * @param page
     * @param resourceBo
     * @return
     */
    @GetMapping
    public CommonsReturn queryResourceData(Page<UmsResource> page, ResourceBo resourceBo){
        QueryWrapper<UmsResource> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(resourceBo.getNameKeyword())){
            queryWrapper.like("name",resourceBo.getNameKeyword());
        }
        if (!StringUtils.isBlank(resourceBo.getUrlKeyword())){
            queryWrapper.eq("url",resourceBo.getUrlKeyword());
        }
        if (resourceBo.getCategoryId()!=null){
            queryWrapper.eq("category_id",resourceBo.getCategoryId());
        }
        IPage<UmsResource> list = resourceService.page(page, queryWrapper);
        return CommonsReturn.success(list);
    }

    /**
     * 新增修改保存
     * @param umsResource
     * @return
     */
    @PostMapping
    @LogsAnnotation("后台资源表 新增修改保存")
    public CommonsReturn saveOrUpdateResource(UmsResource umsResource){
        if (umsResource.getId()==null){
            umsResource.setCreateTime(new Date());
        }
        resourceService.saveOrUpdate(umsResource);
        return CommonsReturn.success();
    }

    /**
     * 回显 根据唯一标识Id查询
     * @param id
     * @return
     */
    @GetMapping("/ById")
    @LogsAnnotation("后台资源表 回显 根据唯一标识Id查询")
    public CommonsReturn queryResourceById(Integer id){
        UmsResource resource = resourceService.getById(id);
        return CommonsReturn.success(resource);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping
    @LogsAnnotation("后台资源表 删除")
    public CommonsReturn deleteById(Integer id){
        resourceService.removeById(id);
        return CommonsReturn.success();
    }

    /**
     * 给用户赋予资源 查询全部
     * @return
     */
    @GetMapping("/All")
    @LogsAnnotation("后台资源表 给用户赋予资源 查询全部")
    public CommonsReturn queryResourceAll(){
        List<UmsResource> list = resourceService.list();
        return CommonsReturn.success(list);
    }
}
