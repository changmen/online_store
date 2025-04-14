CREATE TABLE IF NOT EXISTS brand (
   id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
   name VARCHAR(64) NOT NULL COMMENT '品牌名称',
   description VARCHAR(512) NOT NULL COMMENT '品牌描述',
   logo VARCHAR(128) NOT NULL COMMENT '品牌logo',
   story VARCHAR(1024) NOT NULL COMMENT '品牌故事',
   sort_score INT NOT NULL DEFAULT 100 COMMENT '排行分, 越大越在前',
   visible TINYINT(1) NOT NULL DEFAULT 1 COMMENT '显示状态, 0: 不显示, 1: 显示',
   `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (id),
   KEY `idx_name` (`name`),
   KEY `idx_sort_score` (`sort_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌信息表';