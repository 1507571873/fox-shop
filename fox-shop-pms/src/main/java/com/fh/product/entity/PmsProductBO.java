package com.fh.product.entity;

import lombok.Data;

import java.util.List;

/**
 * @author 尚峰
 * @company 飞狐教育
 * @create 2020-12-13 16:56
 */
@Data
public class PmsProductBO extends PmsProduct {

    //会员价格集合
    private List<PmsMemberPrice> memberPriceList;
    //阶梯价格维护
    private List<PmsProductLadder> productLadderList;
    //商品满减
    private List<PmsProductFullReduction> productFullReductionList;
    //商品属性参数相关列表
    private List<PmsProductAttributeValue> productAttributeValueList;
    //商品sku库存 信 息
    private List<PmsSkuStock> skuStockList;
}
