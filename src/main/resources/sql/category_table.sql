CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(255) NOT NULL COMMENT '类目名称',
    `description` VARCHAR(255) COMMENT '类目描述',
    `parent_id` BIGINT COMMENT '父类目ID',
    `visible` TINYINT(1) DEFAULT 1 COMMENT '是否可见（1：可见，0：不可见）',
    `status` INT COMMENT '状态',
    `weight` INT COMMENT '权重',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX idx_parent_id (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类目表';