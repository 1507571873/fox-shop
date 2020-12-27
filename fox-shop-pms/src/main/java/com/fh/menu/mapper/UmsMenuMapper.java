package com.fh.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.menu.entity.UmsMenu;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-16
 */
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {

    List<UmsMenu> queryTreeByName(String userName);
}
