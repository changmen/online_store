-- 创建商品访问日志表
CREATE TABLE IF NOT EXISTS item_access_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL COMMENT '商品ID',
    user_id VARCHAR(64) COMMENT '用户ID',
    ip VARCHAR(64) COMMENT '访问IP',
    user_agent VARCHAR(512) COMMENT '用户代理',
    referer VARCHAR(512) COMMENT '来源页面',
    access_time DATETIME NOT NULL COMMENT '访问时间',
    access_count INT DEFAULT 1 COMMENT '访问次数',
    session_id VARCHAR(64) COMMENT '会话ID',
    INDEX idx_item_id (item_id),
    INDEX idx_access_time (access_time),
    INDEX idx_user_id (user_id)
) COMMENT='商品访问日志表'; 