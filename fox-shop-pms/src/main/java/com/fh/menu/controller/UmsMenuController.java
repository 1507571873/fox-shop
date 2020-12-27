package com.fh.menu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.menu.entity.UmsMenu;
import com.fh.menu.entity.UmsMenuBo;
import com.fh.menu.service.IUmsMenuService;
import com.fh.utils.CommonsReturn;
import com.fh.utils.SecurityJwtUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-16
 */
@RestController
@RequestMapping("/ums/menu")
public class UmsMenuController {

    @Resource
    private IUmsMenuService menuService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private SecurityJwtUtils securityJwtUtils;
    /**
     * 查询 分页 分级查询
     *
     * @param page
     * @param umsMenuBo
     * @return
     */
    @GetMapping
    public CommonsReturn queryMenuData(Page<UmsMenu> page,UmsMenuBo umsMenuBo) {
        QueryWrapper<UmsMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", umsMenuBo.getLevel());
        //判断pid是否为空 如果为空则说明查看的是一级 不为空则查看这pid下的菜单
        if (umsMenuBo.getPid()!=null){
            queryWrapper.eq("parent_id", umsMenuBo.getPid());
        }
        Page<UmsMenu> page1 = menuService.page(page, queryWrapper);
        return CommonsReturn.success(page1);
    }

    /**
     * 查询 分级查询 用于表单的菜单下拉框
     *
     * @param level
     * @return
     */
    @GetMapping("/level")
    public CommonsReturn queryMenuLevelData(Integer level) {
        QueryWrapper<UmsMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", level);
        List<UmsMenu> list = menuService.list(queryWrapper);
        return CommonsReturn.success(list);
    }

    /**
     * 新增和修改
     *
     * @param umsMenu
     * @return
     */
    @PostMapping
    public CommonsReturn saveOrUpdate(UmsMenu umsMenu) {
        if (umsMenu.getId() == null) {
            umsMenu.setCreateTime(new Date());
            if (umsMenu.getParentId() == 0) {
                umsMenu.setLevel(0);
            } else {
                umsMenu.setLevel(1);
            }
        }
        menuService.saveOrUpdate(umsMenu);
        return CommonsReturn.success();
    }

    /**
     * 回显
     *
     * @param id
     * @return
     */
    @GetMapping("/ById")
    public CommonsReturn queryMenuById(Integer id) {
        UmsMenu menu = menuService.getById(id);
        return CommonsReturn.success(menu);
    }

    /**
     * 菜单树
     * @return CommonsReturn
     */
    @GetMapping("/tree")
    public CommonsReturn queryTreeMenu(){
        List<Map<String,Object>>list=menuService.queryTreeMenu();
        return CommonsReturn.success(list);
    }

    /**
     * 根据用户id查询动态菜单树
     * @return CommonsReturn
     */
    @GetMapping("/TreeByName")
    public CommonsReturn queryTreeByName(){
        String header = request.getHeader("Authorization-token");
        String userName = securityJwtUtils.getUserName(header);
        List<Map<String,Object>>list=menuService.queryTreeByName(userName);
        return CommonsReturn.success(list);
    }
}
