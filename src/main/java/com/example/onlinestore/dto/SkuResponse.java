package com.example.onlinestore.dto;

import com.example.onlinestore.bean.ItemAttributeAndValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SkuResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 260417323154494723L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的商品项ID
     */
    private Long itemId;

    /**
     * SKU唯一编码，用于唯一标识商品库存单元
     */
    private String skuCode;

    /**
     * SKU名称（如："iPhone 15 Pro 256GB 蓝色"）
     */
    private String name;

    /**
     * SKU详细描述文本
     */
    private String description;

    /**
     * 价格（单位：元，精度支持小数点后两位）
     */
    private BigDecimal price;
    /**
     * 标识是否为商品默认选中规格（1=默认规格，0=非默认）
     */
    private Integer defaultSku;

    /**
     * 当前库存数量
     */
    private Integer stockQuantity;
    /**
     * 累计已售出数量
     */
    private Integer soldQuantity;
    /**
     * 库存预警阈值：当 stockQuantity <= warningQuantity 时触发库存警告
     */
    private Integer warningQuantity;

    /**
     * SKU主图URL地址
     */
    private String image;

    /**
     * SKU规格参数列表（如：[{"颜色":"蓝色"}, {"内存":"256GB"}]）
     */
    private List<ItemAttributeAndValue> attributes;

}
