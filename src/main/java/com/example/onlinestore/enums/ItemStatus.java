package com.example.onlinestore.enums;

/**
 * 商品状态枚举，用于表示商品在平台内的生命周期状态及流转过程
 * <p>
 * 状态说明：
 * DRAFT - 草稿状态，商品信息未提交审核的初始可编辑状态
 * PENDING_REVIEW - 待审核状态，商家提交商品后等待平台运营人员审核
 * REJECTED - 驳回状态，平台审核发现信息缺失或违规时的强制退回状态
 * ON_SALE - 在售状态，商品通过审核且处于公开销售阶段
 * OFF_SALE - 手动下架状态，商家主动暂停商品销售的可恢复状态
 * OUT_OF_STOCK - 缺货状态，商品库存归零时自动触发或商家手动标记
 * PRE_SALE - 预售状态，商品接受订单但延迟发货的特殊销售模式
 * BANNED - 禁售状态，平台因严重违规对商品采取的强制管控措施
 * DELETED - 逻辑删除状态，商品数据保留但前端不可见的隐藏状态
 * PENDING_UPDATE - 修改待审状态，商品信息变更后需重新审核的过渡状态
 */
public enum ItemStatus {
    /**
     * 草稿状态
     */
    DRAFT,
    /**
     * 待审核状态
     */
    PENDING_REVIEW,
    /**
     * 审核不通过
     */
    REJECTED,
    /**
     * 在售状态
     */
    ON_SALE,
    /**
     * 手动下架状态
     */
    OFF_SALE,
    /**
     * 缺货状态
     */
    OUT_OF_STOCK,
    /**
     * 预售状态
     */
    PRE_SALE,
    /**
     * 禁售状态
     */
    BANNED,
    /**
     * 逻辑删除状态
     */
    DELETED,
    /**
     * 修改待审状态
     */
    PENDING_UPDATE
}
