-- ============================================
-- 在线商城系统数据库初始化脚本
-- 创建时间: 2025-09-23
-- 说明: 包含所有核心业务表的创建脚本
-- ============================================

-- 使用数据库
USE online_store;

-- 1. 品牌表
CREATE TABLE IF NOT EXISTS brand (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '品牌唯一标识ID',
  `name` VARCHAR(64) NOT NULL COMMENT '品牌名称',
  `logo_url` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '品牌LOGO图片URL',
  `description` VARCHAR(256) DEFAULT '' COMMENT '品牌描述信息',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '品牌状态：ACTIVE-启用/INACTIVE-禁用',
  `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序权重分（越大越靠前）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  INDEX `idx_status_sort` (`status`, `sort_score` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌信息表';

-- 2. 分类表
CREATE TABLE IF NOT EXISTS category (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类唯一标识ID',
  `parent_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `icon_url` VARCHAR(128) DEFAULT '' COMMENT '分类图标URL',
  `description` VARCHAR(256) DEFAULT '' COMMENT '分类描述',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '分类状态：ACTIVE-启用/INACTIVE-禁用',
  `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序权重分（越大越靠前）',
  `level` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '分类层级：1-一级分类，2-二级分类，3-三级分类',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_parent_level` (`parent_id`, `level`),
  INDEX `idx_status_sort` (`status`, `sort_score` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 3. 商品表
CREATE TABLE IF NOT EXISTS item (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品唯一标识ID',
  `brand_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联品牌ID',
  `category_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联类目ID',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '商品名称',
  `description` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '商品详细描述内容，存储在OSS的地址',
  `main_image_url` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '商品主图URL',
  `sub_image_urls` VARCHAR(2048) COMMENT '子图URL集合（多个用逗号分隔）',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ON_SALE' COMMENT '商品状态：ON_SALE-售卖中/OFF_SALE-已下架',
  `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序权重分（越大越靠前）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（ISO8601格式）',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间（ISO8601格式）',
  PRIMARY KEY (`id`),
  INDEX `idx_brand` (`brand_id`),
  INDEX `idx_category_status` (`category_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';

-- 4. 属性表
CREATE TABLE IF NOT EXISTS attribute (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '属性唯一标识ID',
  `name` VARCHAR(64) NOT NULL COMMENT '属性名称（如：颜色、尺寸、材质等）',
  `type` VARCHAR(20) NOT NULL DEFAULT 'SPECIFICATION' COMMENT '属性类型：SPECIFICATION-规格属性/SELLING_POINT-卖点属性',
  `input_type` VARCHAR(20) NOT NULL DEFAULT 'SELECT' COMMENT '输入类型：SELECT-下拉选择/INPUT-手动输入/CHECKBOX-多选',
  `search_type` VARCHAR(20) NOT NULL DEFAULT 'NOT_SEARCH' COMMENT '搜索类型：NOT_SEARCH-不搜索/KEYWORD_SEARCH-关键字搜索/RANGE_SEARCH-范围搜索',
  `related_status` TINYINT NOT NULL DEFAULT 0 COMMENT '是否关联：0-不关联/1-关联（用于生成商品规格）',
  `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序权重分（越大越靠前）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  INDEX `idx_type_sort` (`type`, `sort_score` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- 5. 属性值表
CREATE TABLE IF NOT EXISTS attribute_value (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '属性值唯一标识ID',
  `attribute_id` BIGINT UNSIGNED NOT NULL COMMENT '关联的属性ID',
  `value` VARCHAR(128) NOT NULL COMMENT '属性值内容',
  `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序权重分（越大越靠前）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_attr_value` (`attribute_id`, `value`),
  INDEX `idx_attr_sort` (`attribute_id`, `sort_score` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性值表';

-- 6. 商品属性关联表
CREATE TABLE IF NOT EXISTS item_attribute_relation (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关联关系唯一标识ID',
  `item_id` BIGINT UNSIGNED NOT NULL COMMENT '关联的商品ID',
  `attribute_id` BIGINT UNSIGNED NOT NULL COMMENT '关联的属性ID',
  `attribute_value_id` BIGINT UNSIGNED NOT NULL COMMENT '关联的属性值ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_item_attr_value` (`item_id`, `attribute_id`, `attribute_value_id`),
  INDEX `idx_item` (`item_id`),
  INDEX `idx_attribute` (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性关联表';

-- 7. SKU表
CREATE TABLE IF NOT EXISTS sku (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'SKU唯一标识ID',
  `item_id` BIGINT UNSIGNED NOT NULL COMMENT '关联的商品ID',
  `sku_code` VARCHAR(64) NOT NULL COMMENT 'SKU编码',
  `sku_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'SKU名称',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'SKU价格（单位：元）',
  `original_price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'SKU原价（单位：元）',
  `stock` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存数量',
  `low_stock_threshold` INT UNSIGNED NOT NULL DEFAULT 10 COMMENT '低库存阈值',
  `sku_image_url` VARCHAR(128) DEFAULT '' COMMENT 'SKU图片URL',
  `weight` DECIMAL(8,2) DEFAULT 0.00 COMMENT '重量（单位：kg）',
  `volume` DECIMAL(10,2) DEFAULT 0.00 COMMENT '体积（单位：立方厘米）',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'SKU状态：ACTIVE-启用/INACTIVE-禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku_code` (`sku_code`),
  INDEX `idx_item_status` (`item_id`, `status`),
  INDEX `idx_stock` (`stock`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 8. 会员表
CREATE TABLE IF NOT EXISTS member (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '会员唯一标识ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '加密后的密码',
  `email` VARCHAR(128) DEFAULT '' COMMENT '邮箱地址',
  `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号码',
  `nickname` VARCHAR(64) DEFAULT '' COMMENT '昵称',
  `avatar_url` VARCHAR(128) DEFAULT '' COMMENT '头像URL',
  `gender` VARCHAR(10) DEFAULT 'UNKNOWN' COMMENT '性别：MALE-男/FEMALE-女/UNKNOWN-未知',
  `birthday` DATE COMMENT '生日',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常/INACTIVE-禁用',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员信息表';

-- 9. 商品访问日志表
CREATE TABLE IF NOT EXISTS item_access_log (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '访问日志唯一标识ID',
  `item_id` BIGINT UNSIGNED NOT NULL COMMENT '被访问的商品ID',
  `member_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '访问用户ID（未登录时为NULL）',
  `ip_address` VARCHAR(45) NOT NULL COMMENT '访问者IP地址',
  `user_agent` VARCHAR(512) DEFAULT '' COMMENT '用户代理信息',
  `access_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  `session_id` VARCHAR(128) DEFAULT '' COMMENT '会话ID',
  `referer` VARCHAR(512) DEFAULT '' COMMENT '来源页面URL',
  PRIMARY KEY (`id`),
  INDEX `idx_item_time` (`item_id`, `access_time`),
  INDEX `idx_member_time` (`member_id`, `access_time`),
  INDEX `idx_access_time` (`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品访问日志表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入默认分类数据
INSERT IGNORE INTO category (id, parent_id, name, description, level, sort_score) VALUES
(1, 0, '服装鞋帽', '各类服装、鞋子、帽子等时尚单品', 1, 100),
(2, 0, '数码电器', '手机、电脑、家电等数码产品', 1, 90),
(3, 0, '家居生活', '家具、装饰、日用品等家居用品', 1, 80),
(4, 1, '男装', '男士服装', 2, 50),
(5, 1, '女装', '女士服装', 2, 60),
(6, 2, '手机通讯', '各品牌手机及配件', 2, 50),
(7, 2, '电脑办公', '笔记本、台式机、办公设备', 2, 40);

-- 插入默认品牌数据
INSERT IGNORE INTO brand (id, name, description, sort_score) VALUES
(1, '苹果', 'Apple Inc. - 创新科技领导者', 100),
(2, '华为', 'HUAWEI - 智能世界的构建者', 90),
(3, '小米', 'MI - 让每个人都能享受科技的乐趣', 80),
(4, '耐克', 'NIKE - Just Do It', 70),
(5, '阿迪达斯', 'adidas - Impossible is Nothing', 60);

-- 插入默认属性数据
INSERT IGNORE INTO attribute (id, name, type, input_type, related_status, sort_score) VALUES
(1, '颜色', 'SPECIFICATION', 'SELECT', 1, 100),
(2, '尺寸', 'SPECIFICATION', 'SELECT', 1, 90),
(3, '材质', 'SPECIFICATION', 'SELECT', 0, 80),
(4, '重量', 'SPECIFICATION', 'INPUT', 0, 70),
(5, '产地', 'SELLING_POINT', 'SELECT', 0, 60);

-- 插入默认属性值数据
INSERT IGNORE INTO attribute_value (attribute_id, value, sort_score) VALUES
-- 颜色属性值
(1, '黑色', 100), (1, '白色', 90), (1, '红色', 80), (1, '蓝色', 70), (1, '绿色', 60),
-- 尺寸属性值
(2, 'S', 100), (2, 'M', 90), (2, 'L', 80), (2, 'XL', 70), (2, 'XXL', 60),
-- 材质属性值
(3, '棉质', 100), (3, '聚酯纤维', 90), (3, '羊毛', 80), (3, '丝绸', 70),
-- 产地属性值
(5, '中国', 100), (5, '美国', 90), (5, '德国', 80), (5, '日本', 70);

-- ============================================
-- 数据库初始化完成
-- ============================================