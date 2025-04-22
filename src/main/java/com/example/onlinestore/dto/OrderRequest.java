package com.example.onlinestore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;

    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemRequest> items;

    private String remark;
} 