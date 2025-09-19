package com.example.onlinestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建地址请求DTO
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CreateAddressRequest implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1234567890123456789L;
    
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String receiverName;
    
    /**
     * 收货人电话
     */
    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String receiverPhone;
    
    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份名称长度不能超过20个字符")
    private String province;
    
    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市名称长度不能超过20个字符")
    private String city;
    
    /**
     * 区县
     */
    @NotBlank(message = "区县不能为空")
    @Size(max = 20, message = "区县名称长度不能超过20个字符")
    private String district;
    
    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200个字符")
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确")
    private String postalCode;
    
    /**
     * 是否为默认地址
     */
    private Boolean isDefault;
    
    /**
     * 地址标签
     */
    @Size(max = 20, message = "地址标签长度不能超过20个字符")
    private String addressLabel;
}