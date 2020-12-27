package com.fh.category.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.category.entity.PmsProductCategory;
import com.fh.category.service.IPmsProductCategoryService;
import com.fh.config.LogsAnnotation;
import com.fh.model.search.CategorySearch;
import com.fh.utils.CommonsReturn;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/pms/category")
public class PmsProductCategoryController {

    @Resource
    private IPmsProductCategoryService categoryService;

    /**
     * 查询 分页查询
     * 一二级节点判断
     * @param page-
     * @param search-
     * @return CommonsReturn
     */
    @GetMapping
    public CommonsReturn queryCategoryData(Page<PmsProductCategory> page, CategorySearch search){
        //生成条件过滤器
        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<PmsProductCategory>();
        //判断search里传过来的pid是否为空 为空则说明他是父节点
        if (search.getPid() !=null){
            queryWrapper.eq("parent_Id",search.getPid());
        }else {
            queryWrapper.eq("level",search.getLevel());
        }
        IPage<PmsProductCategory> list = categoryService.page(page,queryWrapper);
        return CommonsReturn.success(list);
    }

    /**
     * 新增Or修改
     * 这个方法需要判断前台传过来的ParentId
     * @param pmsProductCategory-
     * @return CommonsReturn
     */
    @PostMapping
    @ApiOperation("商品分类 新增Or修改")
    public CommonsReturn saveOrUpdateCategoryData(PmsProductCategory pmsProductCategory){
            //判断前台传过来的节点id是否为-1
            if(pmsProductCategory.getParentId() == -1){
                //如果为-1 则说明是一级节点 把他的level和ParentId重新赋值为0
                pmsProductCategory.setLevel(0);
                pmsProductCategory.setParentId(0l);
            }else{
                //如果不等于-1 则说明他是二级节点 则 把他的level设置为 1
                pmsProductCategory.setLevel(1);
            }
            //保存
            categoryService.saveOrUpdate(pmsProductCategory);
        return CommonsReturn.success();
    }

    /**
     * 查询下拉框
     * @return CommonsReturn
     */
    @GetMapping("/parent")
    @ApiOperation("商品分类 查询下拉框")
    public CommonsReturn queryParentCategoryList(){
        QueryWrapper<PmsProductCategory> queryWrapper=new QueryWrapper<PmsProductCategory>();
        queryWrapper.eq("level",0);
        List<PmsProductCategory> categoryList = categoryService.list(queryWrapper);
        return CommonsReturn.success(categoryList);
    }

    /**
     * 修改的数据回显 根据唯一标识Id去查询
     * @param id-
     * @return CommonsReturn
     */
    @GetMapping("/ById")
    @LogsAnnotation("商品分类 修改的数据回显 根据唯一标识Id去查询")
    public CommonsReturn queryParentCategoryById(Integer id){
        PmsProductCategory category = categoryService.getById(id);
        if (category.getParentId()==0){
            category.setParentId(-1l);
        }
        return CommonsReturn.success(category);
    }

    /**
     * 查询 商品分类 用于商品维护中的form表单 里的商品信息的下拉框
     * @return CommonsReturn
     */
    @GetMapping("All")
    public CommonsReturn queryParentCategoryAll(){
        List<Map<String,Object>> list =categoryService.queryParentCategoryAll();
        return CommonsReturn.success(list);
    }
}
