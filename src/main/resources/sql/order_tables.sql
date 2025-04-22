-- 订单主表
CREATE TABLE `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    shipping_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费',
    address_id BIGINT NOT NULL COMMENT '收货地址ID',
    status VARCHAR(20) NOT NULL COMMENT '订单状态：PENDING_PAYMENT-待支付，PAID-已支付，SHIPPED-已发货，COMPLETED-已完成，CANCELLED-已取消，REFUNDING-退款中，REFUNDED-已退款',
    payment_method VARCHAR(20) COMMENT '支付方式：ALIPAY-支付宝，WECHAT-微信',
    payment_time DATETIME COMMENT '支付时间',
    shipping_time DATETIME COMMENT '发货时间',
    completion_time DATETIME COMMENT '完成时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(255) COMMENT '取消原因',
    remark VARCHAR(255) COMMENT '订单备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_member_id (member_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 订单明细表
CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    sku_id BIGINT NOT NULL COMMENT '商品SKU ID',
    sku_code VARCHAR(32) NOT NULL COMMENT '商品SKU编码',
    sku_name VARCHAR(128) NOT NULL COMMENT '商品SKU名称',
    sku_image VARCHAR(255) COMMENT '商品SKU图片',
    price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_sku_id (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 订单支付记录表
CREATE TABLE order_payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    payment_no VARCHAR(32) NOT NULL COMMENT '支付流水号',
    payment_method VARCHAR(20) NOT NULL COMMENT '支付方式：ALIPAY-支付宝，WECHAT-微信',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status VARCHAR(20) NOT NULL COMMENT '支付状态：PENDING-待支付，SUCCESS-支付成功，FAILED-支付失败',
    payment_time DATETIME COMMENT '支付时间',
    third_party_payment_no VARCHAR(64) COMMENT '第三方支付流水号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_payment_no (payment_no),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单支付记录表';

-- 订单退款记录表
CREATE TABLE order_refund (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    refund_no VARCHAR(32) NOT NULL COMMENT '退款流水号',
    payment_id BIGINT NOT NULL COMMENT '支付记录ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    reason VARCHAR(255) NOT NULL COMMENT '退款原因',
    status VARCHAR(20) NOT NULL COMMENT '退款状态：PENDING-待退款，SUCCESS-退款成功，FAILED-退款失败',
    refund_time DATETIME COMMENT '退款时间',
    third_party_refund_no VARCHAR(64) COMMENT '第三方退款流水号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_refund_no (refund_no),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单退款记录表'; 