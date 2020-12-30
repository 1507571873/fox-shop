package com.fh.brand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.brand.entity.PmsBrand;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-08
 */
public interface PmsBrandMapper extends BaseMapper<PmsBrand> {

    List<Map<String, Object>> queryBrandName();
}
