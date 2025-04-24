-- 积分规则表
CREATE TABLE point_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(64) NOT NULL COMMENT '规则名称',
    description VARCHAR(128) COMMENT '规则描述',
    points DECIMAL(10,2) NOT NULL COMMENT '积分数量',
    status VARCHAR(10) NOT NULL DEFAULT 'ENABLE' COMMENT '状态：ENABLE-启用，DISABLE-禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name (name),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分规则表';

-- 积分记录表
CREATE TABLE point_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    order_no  VARCHAR(32) COMMENT '订单编号',
    points DECIMAL(10,2) NOT NULL COMMENT '积分数量',
    type VARCHAR(10) NOT NULL COMMENT '类型：EARN-获得积分，CONSUME-消费积分',
    description VARCHAR(128) COMMENT '描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_member_id (member_id),
    INDEX idx_order_no (order_no),
    INDEX idx_created_at (created_at),
    INDEX idx_member_created (member_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表'; 