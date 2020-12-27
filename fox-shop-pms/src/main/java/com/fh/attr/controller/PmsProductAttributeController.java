package com.fh.attr.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.attr.entity.PmsProductAttribute;
import com.fh.attr.service.IPmsProductAttributeService;
import com.fh.model.search.AttrSearch;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品属性参数表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/pms/attr/attribute")
public class PmsProductAttributeController {

    @Resource
    private IPmsProductAttributeService productAttributeService;

    /**
     * 查询 分页查询
     *
     * @return CommonsReturn
     */
    @GetMapping
    public CommonsReturn queryProductAttributeData(Page<PmsProductAttribute> page, AttrSearch attrSearch) {
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<PmsProductAttribute>();
        if (StringUtils.isBlank(attrSearch.getName())) {
            queryWrapper.like("name", attrSearch.getName());
        }
        queryWrapper.eq("product_attribute_category_id", attrSearch.getCid());
        queryWrapper.eq("type", attrSearch.getType());
        IPage<PmsProductAttribute> attributePage = productAttributeService.page(page, queryWrapper);
        return CommonsReturn.success(attributePage);
    }

    /**
     * 查询所有数据
     *
     * @return CommonsReturn
     */
    @GetMapping("All")
    public CommonsReturn queryProductAttributeAll(Page<PmsProductAttribute> page, AttrSearch attrSearch) {
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<PmsProductAttribute>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.eq("product_attribute_category_id", attrSearch.getCid());
        queryWrapper.eq("type", attrSearch.getType());
        IPage<PmsProductAttribute> attributePage = productAttributeService.page(page, queryWrapper);
        return CommonsReturn.success(attributePage);
    }

    /**
     * 新增和修改的提交方法
     *
     * @param pmsProductAttribute
     * @return CommonsReturn
     */
    @PostMapping
    public CommonsReturn saveOrUpdateAttributeData(PmsProductAttribute pmsProductAttribute) {
        productAttributeService.saveOrUpdate(pmsProductAttribute);
        return CommonsReturn.success();
    }

    /**
     * 修改回显的方法 根据唯一标识Id进行查询
     *
     * @param id
     * @return CommonsReturn
     */
    @GetMapping("/ById")
    public CommonsReturn queryProductAttributeById(Integer id) {
        PmsProductAttribute attributeServiceById = productAttributeService.getById(id);
        return CommonsReturn.success(attributeServiceById);
    }
}
