package com.fh.attr.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.attr.entity.PmsProductAttributeCategory;
import com.fh.attr.service.IPmsProductAttributeCategoryService;
import com.fh.config.LogsAnnotation;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 产品属性分类表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/pms/attr/category")
public class PmsProductAttributeCategoryController {

    @Resource
    private IPmsProductAttributeCategoryService pmsProductAttributeCategoryService;

    /**
     * 查询 分页查询 条件查询
     * @param page
     * @return CommonsReturn
     */
    @GetMapping
    public CommonsReturn queryCategoryData(Page<PmsProductAttributeCategory> page){
        IPage<PmsProductAttributeCategory> attributeCategoryPage = pmsProductAttributeCategoryService.page(page);
        return CommonsReturn.success(attributeCategoryPage);
    }

    /**
     * 新增or修改的方法
     * @param pmsProductAttributeCategory
     * @return CommonsReturn
     */
    @PostMapping
    public CommonsReturn saveOrUpdateCategory(PmsProductAttributeCategory pmsProductAttributeCategory){
        pmsProductAttributeCategoryService.saveOrUpdate(pmsProductAttributeCategory);
        return CommonsReturn.success();
    }

    /**
     * 修改的回显方法 根据唯一标识Id进行查询
     * @param id
     * @return CommonsReturn
     */
    @GetMapping("/ById")
    public CommonsReturn queryCategoryDataById(Integer id){
        PmsProductAttributeCategory data = pmsProductAttributeCategoryService.getById(id);
        return CommonsReturn.success(data);
    }
}
