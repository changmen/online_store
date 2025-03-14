-- 创建类目属性表
CREATE TABLE `category_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `category_id` bigint(20) NOT NULL COMMENT '类目ID',
  `name` varchar(50) NOT NULL COMMENT '属性名称',
  `value_type` tinyint(4) NOT NULL COMMENT '属性值类型：1-文本，2-数字，3-日期，4-枚举，5-布尔',
  `required` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否必填：0-否，1-是',
  `searchable` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否可搜索：0-否，1-是',
  `multi_value` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否多选：0-否，1-是（仅当value_type=4时有效）',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值',
  `unit` varchar(20) DEFAULT NULL COMMENT '属性单位',
  `options` text COMMENT 'JSON格式的可选值列表（仅当value_type=4时有效）',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_attribute_name` (`category_id`,`name`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_value_type` (`value_type`),
  KEY `idx_required` (`required`),
  KEY `idx_searchable` (`searchable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类目属性表';

-- 添加外键约束（如果需要）
-- ALTER TABLE `category_attribute` ADD CONSTRAINT `fk_category_attribute_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE; 