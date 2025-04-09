package com.example.onlinestore.service;

import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.CreateSkuRequest;
import jakarta.validation.Valid;
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
     * @return 成功创建的SKU实体对象，包含系统生成的唯一标识符（如SKU ID）、
     *         创建时间戳等持久化信息
     */
    Sku createSku(@Valid CreateSkuRequest createSkuRequest);


    /**
     * 根据商品ID获取SKU列表
     *
     * @param itemId 商品ID
     * @return SKU列表
     */
    List<Sku> getSkusByItemId(@NotNull Long itemId);

    /**
     * 更新SKU库存数量
     *
     * @param skuId   SKU ID
     * @param quantity 库存数量
     */
    void updateStockQuantity(@NotNull Long skuId, @NotNull Integer quantity);

    /**
     * 根据SKU ID获取SKU详情
     *
     * @param skuId SKU ID
     * @return SKU 实体对象, 包含属性值
     */
    Sku getSkuById(@NotNull Long skuId);
    /**
     * 检查SKU库存是否充足
     *
     * @param id SKU ID
     * @param quantity 需要检查的数量
     * @return 是否充足 true:充足，false:不足
     */
    boolean checkStock(@NotNull Long id, @NotNull Integer quantity);
}
