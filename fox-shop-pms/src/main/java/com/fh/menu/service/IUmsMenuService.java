package com.fh.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fh.menu.entity.UmsMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-16
 */
public interface IUmsMenuService extends IService<UmsMenu> {

    List<Map<String, Object>> queryTreeMenu();

    List<Map<String, Object>> queryTreeByName(String userName);
}
