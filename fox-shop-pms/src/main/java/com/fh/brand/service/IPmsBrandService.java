package com.fh.brand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fh.brand.entity.PmsBrand;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-08
 */
public interface IPmsBrandService extends IService<PmsBrand> {

    List<Map<String, Object>> queryBrandName();
}
