package com.fh.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.menu.entity.UmsMenu;
import com.fh.menu.mapper.UmsMenuMapper;
import com.fh.menu.service.IUmsMenuService;
import com.fh.resource.entity.UmsResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-16
 */
@Service
public class UmsMenuServiceImpl extends ServiceImpl<UmsMenuMapper, UmsMenu> implements IUmsMenuService {

    @Resource
    private UmsMenuMapper menuMapper;
    @Override
    public List<Map<String, Object>> queryTreeMenu() {
        List<UmsMenu> listMenu = super.list();
        return getMenuTreeList(listMenu,0l);
    }

    private List<Map<String, Object>> getMenuTreeList(List<UmsMenu> listMenu, Long pid) {
        List<Map<String, Object>> list= new ArrayList<>();
        listMenu.forEach(umsMenu->{
            Map<String, Object> map = null;
            if (umsMenu.getParentId().equals(pid)){
                map=new HashMap<>();
                map.put("id",umsMenu.getId());
                map.put("label",umsMenu.getTitle());
                if (umsMenu.getLevel()==0){
                    map.put("children",getMenuTreeList(listMenu,umsMenu.getId()));
                }
            }
            if (map!=null){
                list.add(map);
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> queryTreeByName(String userName) {
        List<UmsMenu> menuList = menuMapper.queryTreeByName(userName);
        return getTreeByName(menuList,0l);
    }

    private List<Map<String, Object>> getTreeByName(List<UmsMenu> menuList, Long pid) {
        List<Map<String, Object>> list=new ArrayList<>();
        menuList.forEach(menu->{
            Map<String, Object> map = null;
            if (menu.getParentId().equals(pid)){
                map=new HashMap<>();
                map.put("index",menu.getName());
                map.put("icon",menu.getIcon());
                map.put("title",menu.getTitle());
                if (menu.getParentId()==0){
                    map.put("subs",getTreeByName(menuList,menu.getId()));
                }
            }
            if (map!=null){
                list.add(map);
            }
        });
        return list;
    }
}
