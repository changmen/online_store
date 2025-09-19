package com.example.onlinestore.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1099483498189107703L;

    private Long id;
    
    // 用户ID
    private Long memberId;
    
    // 收货人姓名
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;
    
    // 收货人电话
    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;
    
    // 省份
    @NotBlank(message = "省份不能为空")
    private String province;
    
    // 城市
    @NotBlank(message = "城市不能为空")
    private String city;
    
    // 区/县
    @NotBlank(message = "区/县不能为空")
    private String district;
    
    // 详细地址
    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;
    
    // 是否为默认地址
    private boolean isDefault;
}