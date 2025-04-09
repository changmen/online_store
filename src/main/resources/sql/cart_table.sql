CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `member_id` BIGINT NOT NULL COMMENT '会员ID',
    `item_id` BIGINT UNSIGNED NOT NULL COMMENT '关联商品项ID，对应item表主键',
    `sku_id` BIGINT NOT NULL COMMENT '商品SKU ID',
    `quantity` INT NOT NULL COMMENT '商品数量',
    `price` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '商品总价',
    `selected` TINYINT(1) DEFAULT 1 COMMENT '是否选中（1：选中，0：未选中）',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_sku` (`member_id`, `sku_id`),
    KEY `idx_member_id_item_id` (`member_id`, `item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表'; 