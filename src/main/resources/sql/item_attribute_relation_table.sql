CREATE TABLE item_attribute_relation (
     `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `item_id` BIGINT UNSIGNED NOT NULL COMMENT '关联商品项ID，对应item表主键',
     `attribute_id` BIGINT UNSIGNED NOT NULL COMMENT '关联属性ID，对应attribute表主键',
     `value_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '预定义属性值ID，对应attribute_value表主键',
     `input_value` VARCHAR(255) DEFAULT NULL COMMENT '用户自定义输入值（当value_id为空时有效）',
     `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY uk_item_attribute (`item_id`, `attribute_id`),
     INDEX idx_item (`item_id`),
     INDEX idx_attribute (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品项-属性关联表（支持预定义值和自定义值两种存储方式）';
