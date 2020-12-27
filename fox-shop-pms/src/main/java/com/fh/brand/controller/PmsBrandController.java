package com.fh.brand.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.brand.entity.PmsBrand;
import com.fh.brand.service.IPmsBrandService;
import com.fh.config.LogsAnnotation;
import com.fh.utils.CommonsReturn;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-08
 */
@RestController
@RequestMapping("/pms/brand")
public class PmsBrandController {

    @Resource
    private IPmsBrandService brandService;

    /**
     * 查询 分页查询 
     * @param page
     * @return
     */
    @GetMapping
    public CommonsReturn queryBrandTable(Page<PmsBrand> page){
        IPage<PmsBrand> iPage =brandService.page(page);
        return CommonsReturn.success(iPage);
    }

    /**
     * 查询 全部数据 用于商品维护里的form表单的商品品牌的下拉框
     * @return CommonsReturn
     */
    @GetMapping("/All")
    public CommonsReturn queryBrandAll(){
        List<PmsBrand> list = brandService.list();
        return CommonsReturn.success(list);
    }

    /**
     * 根据id进行删除
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("商品管理 根据id进行删除")
    public CommonsReturn deleteBrandData(Long id){
        brandService.removeById(id);
        return CommonsReturn.success();
    }

    /**
     * 新增or修改
     * @param pmsBrand
     * @return
     */
    @PostMapping
    public CommonsReturn saveBrandData(PmsBrand pmsBrand){
        brandService.saveOrUpdate(pmsBrand);
        return CommonsReturn.success();
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @GetMapping("/update")
    @ApiOperation("商品管理 回显的方法")
    public CommonsReturn queryBrandById(Integer id){
        PmsBrand brand = brandService.getById(id);
        return CommonsReturn.success(brand);
    }

}
