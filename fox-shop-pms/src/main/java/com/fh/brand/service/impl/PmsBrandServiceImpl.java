package com.fh.brand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.brand.entity.PmsBrand;
import com.fh.brand.mapper.PmsBrandMapper;
import com.fh.brand.service.IPmsBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-08
 */
@Service
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements IPmsBrandService {

    @Resource
    private PmsBrandMapper pmsBrandMapper;
    @Override
    public List<Map<String, Object>> queryBrandName() {
        return pmsBrandMapper.queryBrandName();
    }
}
