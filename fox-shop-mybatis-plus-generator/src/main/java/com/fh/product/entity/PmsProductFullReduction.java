package com.fh.product.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 产品满减表(只针对同商品)
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductFullReduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long productId;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;


}
