package com.example.onlinestore.service;

import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.CreateSkuRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface SkuService {

    /**
     * 创建新的SKU（库存单位）实例
     *
     * @param createSkuRequest 包含SKU创建信息的请求对象，需通过@Valid注解进行参数校验
     *                         应包含以下必要信息：
     *                         - 商品基础属性（如名称、规格等）
     *                         - 库存管理参数（如初始库存量、预警阈值等）
     *                         - 价格策略信息（如成本价、销售价等）
     * @return 成功创建的SKU实体对象，包含SkuId
     * @throws com.example.onlinestore.exceptions.BizException 如果创建过程中发生任何错误，将抛出该业务异常
     */
    Sku createSku(@NotNull @Valid CreateSkuRequest createSkuRequest);


    /**
     * 根据商品ID获取SKU列表
     *
     * @param itemId 商品ID
     * @return SKU列表
     * @throws com.example.onlinestore.exceptions.BizException 如果获取过程中发生访问DB等错误时候，将抛出该业务异常
     */
    List<Sku> getSkusByItemId(@NotNull Long itemId);

    /**
     * 更新SKU库存数量
     *
     * @param skuId    SKU ID
     * @param quantity 库存数量
     * @throws com.example.onlinestore.exceptions.BizException 如果根据SkuId查询不到Sku，或者校验失败，访问DB失败，将抛出该业务异常
     */
    void updateStockQuantity(@NotNull Long skuId, @NotNull @Min(value = 1, message = "库存数量必须大于0") Integer quantity);

    /**
     * 根据SKU ID获取SKU详情
     *
     * @param skuId SKU ID
     * @return SKU 实体对象, 包含属性值
     * @throws com.example.onlinestore.exceptions.BizException 如果根据SkuId查询不到Sku，或者访问DB失败，将抛出该业务异常
     */
    Sku getSkuById(@NotNull Long skuId);

}
