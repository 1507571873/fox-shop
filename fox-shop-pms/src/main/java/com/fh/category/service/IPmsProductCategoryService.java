package com.fh.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fh.category.entity.PmsProductCategory;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-09
 */
public interface IPmsProductCategoryService extends IService<PmsProductCategory> {

    /**
     * 查询 商品分类 用于商品维护中的form表单 里的商品信息的下拉框
     * @return
     */
    List<Map<String, Object>> queryParentCategoryAll();
}
