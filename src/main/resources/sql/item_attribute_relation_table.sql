CREATE TABLE item_attribute_relation (
     `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `item_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联商品项ID，对应item表主键',
     `sku_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联商品SKU ID，对应sku表主键, 当属性为SKU的时候，sku_id必填',
     `attribute_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联属性ID，对应attribute表主键',
     `value_id` BIGINT UNSIGNED DEFAULT 0 COMMENT '预定义属性值ID，对应attribute_value表主键',
     `input_value` VARCHAR(255) DEFAULT '' COMMENT '用户自定义输入值（当value_id为空时有效）',
     `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`),
     UNIQUE idx_item_id_sku_id_attribute_id (`item_id`, `sku_id`, `attribute_id`),
     INDEX idx_attribute (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品项-属性关联表（支持预定义值和自定义值两种存储方式）';
