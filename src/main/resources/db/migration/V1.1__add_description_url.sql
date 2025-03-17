-- 添加商品描述URL字段
ALTER TABLE items ADD COLUMN description_url VARCHAR(255) COMMENT '商品描述OSS URL'; 