package com.fh.product.service.impl;

import com.fh.product.entity.PmsProduct;
import com.fh.product.mapper.PmsProductMapper;
import com.fh.product.service.IPmsProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-13
 */
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements IPmsProductService {

}
