package com.fh.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.product.entity.PmsSkuStock;
import com.fh.product.mapper.PmsSkuStockMapper;
import com.fh.product.service.IPmsSkuStockService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-15
 */
@Service
public class PmsSkuStockServiceImpl extends ServiceImpl<PmsSkuStockMapper, PmsSkuStock> implements IPmsSkuStockService {

}
