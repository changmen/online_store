CREATE TABLE attribute (
       `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
       `name` VARCHAR(50) NOT NULL COMMENT '属性名称',
       `attribute_type` VARCHAR(20) NOT NULL COMMENT '属性类型（见枚举AttributeType）',
       `required` TINYINT NOT NULL DEFAULT 0 COMMENT '是否必填：0-否 1-是',
       `searchable` TINYINT NOT NULL DEFAULT 0 COMMENT '是否可搜索：0-否 1-是',
       `input_type` VARCHAR(20) NOT NULL COMMENT '输入类型（见枚举AttributeInputType）',
       `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序分值（越大越靠前）',
       `visible` TINYINT NOT NULL DEFAULT 0 COMMENT '是否可见：0-否 1-是',
       `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`),
       UNIQUE INDEX udx_name (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性表';