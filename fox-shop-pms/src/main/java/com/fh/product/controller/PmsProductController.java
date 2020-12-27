package com.fh.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fh.product.entity.PmsProduct;
import com.fh.product.entity.PmsProductBO;
import com.fh.product.service.IPmsProductService;
import com.fh.utils.CommonsReturn;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-13
 */
@RestController
@RequestMapping("/pms/product")
public class PmsProductController {

    @Resource
    private IPmsProductService pmsProductService;


    /**
     * 查询 分页查询
     * @param page
     * @return CommonsReturn
     */
    @GetMapping
    public CommonsReturn queryProductData(Page<PmsProduct> page){
        IPage<PmsProduct> list = pmsProductService.page(page);
        return CommonsReturn.success(list);
    }

    /**
     * 商品维护新增
     * @param pmsProductBO
     * @return CommonsReturn
     */
    @PostMapping
    public CommonsReturn saveOrUpdateProductData(@RequestBody PmsProductBO pmsProductBO){
        pmsProductService.createProduct(pmsProductBO);
        return CommonsReturn.success();
    }

    /**
     * 回显数据
     * @param id
     * @return CommonsReturn
     */
    @GetMapping("/ById")
    public CommonsReturn queryProductById(Long id){
        PmsProductBO pmsProductBO = pmsProductService.queryProductById(id);
        return CommonsReturn.success(pmsProductBO);
    }
}
