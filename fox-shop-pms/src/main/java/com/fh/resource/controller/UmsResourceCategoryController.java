package com.fh.resource.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.resource.entity.UmsResourceCategory;
import com.fh.resource.service.IUmsResourceCategoryService;
import com.fh.utils.CommonsReturn;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源分类表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/ums/resourceCategory")
public class UmsResourceCategoryController {

    @Resource
    private IUmsResourceCategoryService resourceCategoryService;

    /**
     * 查询所有资源分类 用于资源列表页面的条件查询 和表单
     * @return
     */
    @GetMapping("/All")
    public CommonsReturn queryResourceCategoryAll(){
        List<UmsResourceCategory> list = resourceCategoryService.list();
        return CommonsReturn.success(list);
    }

    /**
     * 查询分页查询 用于资源分类的列表查询
     * @param page
     * @return
     */
    @GetMapping
    public CommonsReturn queryResourceCategoryData(Page<UmsResourceCategory> page){
        Page<UmsResourceCategory> categoryPage = resourceCategoryService.page(page);
        return CommonsReturn.success(categoryPage);
    }

    /**
     * 资源分类 的新增和修改
     * @param umsResourceCategory
     * @return
     */
    @PostMapping
    public CommonsReturn saveOrUpdate(UmsResourceCategory umsResourceCategory){
        if (umsResourceCategory.getId()==null){
            umsResourceCategory.setCreateTime(new Date());
        }
        resourceCategoryService.saveOrUpdate(umsResourceCategory);
        return CommonsReturn.success();
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @GetMapping("/ById")
    public CommonsReturn queryResourceCategoryById(Integer id){
        UmsResourceCategory category = resourceCategoryService.getById(id);
        return CommonsReturn.success(category);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping
    public CommonsReturn deleteById(Integer id){
        resourceCategoryService.removeById(id);
        return CommonsReturn.success();
    }
}
