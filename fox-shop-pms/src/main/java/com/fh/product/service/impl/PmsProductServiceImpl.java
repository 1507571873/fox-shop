package com.fh.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.product.entity.*;
import com.fh.product.mapper.PmsProductMapper;
import com.fh.product.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;


import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private IPmsMemberPriceService memberPriceService;
    @Resource
    private IPmsProductLadderService pmsProductLadderService;
    @Resource
    private IPmsProductFullReductionService pmsProductFullReductionService;
    @Resource
    private IPmsProductAttributeValueService pmsProductAttributeValueService;
    @Resource
    private IPmsSkuStockService skuStockService;

    /**
     * @param pmsProductBO
     */
    @Override
    @Transactional
    public void createProduct(PmsProductBO pmsProductBO) {
        PmsProduct pmsProduct = pmsProductBO;
        boolean status = super.saveOrUpdate(pmsProduct);
        if (status) {
            Long productId = pmsProduct.getId();
            //保存会员价格
            saveMemberPrice(pmsProductBO.getMemberPriceList(), productId);
            //阶梯价格维护
            saveProductLadder(pmsProductBO.getProductLadderList(), productId);
            //商品满减
            saveProductFullReduction(pmsProductBO.getProductFullReductionList(), productId);
            //商品属性参数相关列表
            saveProductAttributeValue(pmsProductBO.getProductAttributeValueList(), productId);
            //商品sku库存 信 息
            saveSkuStock(pmsProductBO.getSkuStockList(), productId);
        }

    }

    /**
     * 查询商品信息
     *
     * @param id
     * @return
     */
    @Override
    public PmsProductBO queryProductById(Long id) {
        //查询商品信息
        PmsProductBO pmsProductBO = new PmsProductBO();
        BeanUtils.copyProperties(super.getById(id), pmsProductBO);
        Map<String, Object> map = new HashMap<>(100);
        map.put("product_id", id);
        //保存会员价格
        List<PmsMemberPrice> pmsMemberPrices = memberPriceService.listByMap(map);
        //阶梯价格维护
        List<PmsProductLadder> pmsProductLadders = pmsProductLadderService.listByMap(map);
        //商品满减
        List<PmsProductFullReduction> pmsProductFullReductions = pmsProductFullReductionService.listByMap(map);
        //商品属性参数相关列表
        List<PmsProductAttributeValue> pmsProductAttributeValues = pmsProductAttributeValueService.listByMap(map);
        //商品sku库存 信 息
        List<PmsSkuStock> pmsSkuStocks = skuStockService.listByMap(map);
        pmsProductBO.setMemberPriceList(pmsMemberPrices);
        pmsProductBO.setProductLadderList(pmsProductLadders);
        pmsProductBO.setProductFullReductionList(pmsProductFullReductions);
        pmsProductBO.setProductAttributeValueList(pmsProductAttributeValues);
        pmsProductBO.setSkuStockList(pmsSkuStocks);
        return pmsProductBO;
    }

    /**
     * 商品sku库存 信 息
     *
     * @param skuStockList
     * @param productId
     */
    private void saveSkuStock(List<PmsSkuStock> skuStockList, Long productId) {
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock pmsSkuStock = skuStockList.get(i);
            pmsSkuStock.setProductId(productId);
            String dateStr = DateUtils.formatDate(new Date(), "yyyyMMdd");
            String productCode = String.format("%06d", productId);
            String skuCode = String.format("%03d", i + 1);
            pmsSkuStock.setSkuCode(StringUtils.join( productCode,dateStr,skuCode));
        }
        skuStockService.remove(new QueryWrapper<PmsSkuStock>().eq("product_id", productId));
        skuStockService.saveBatch(skuStockList);
    }


    /**
     * 商品属性参数相关列表
     *
     * @param productAttributeValueList
     * @param productId
     */
    private void saveProductAttributeValue(List<PmsProductAttributeValue> productAttributeValueList, Long productId) {
        productAttributeValueList.forEach(data -> {
            data.setProductId(productId);
        });
        pmsProductAttributeValueService.remove(new QueryWrapper<PmsProductAttributeValue>().eq("product_id", productId));
        pmsProductAttributeValueService.saveBatch(productAttributeValueList);
    }

    /**
     * 商品满减
     *
     * @param productFullReductionList
     * @param productId
     */
    private void saveProductFullReduction(List<PmsProductFullReduction> productFullReductionList, Long productId) {
        productFullReductionList.forEach(data -> {
            data.setProductId(productId);
        });
        pmsProductFullReductionService.remove(new QueryWrapper<PmsProductFullReduction>().eq("product_id", productId));
        pmsProductFullReductionService.saveBatch(productFullReductionList);
    }

    /**
     * 阶梯价格维护
     *
     * @param productLadderList
     * @param productId
     */
    private void saveProductLadder(List<PmsProductLadder> productLadderList, Long productId) {
        productLadderList.forEach(data -> {
            data.setProductId(productId);
        });
        pmsProductLadderService.remove(new QueryWrapper<PmsProductLadder>().eq("product_id", productId));
        pmsProductLadderService.saveBatch(productLadderList);
    }

    /**
     * 保存会员价格
     *
     * @param list
     * @param productId
     */
    private void saveMemberPrice(List<PmsMemberPrice> list, Long productId) {
        list.forEach(memberPrice -> {
            memberPrice.setProductId(productId);
        });
        memberPriceService.remove(new QueryWrapper<PmsMemberPrice>().eq("product_id", productId));
        memberPriceService.saveBatch(list);
    }
}
