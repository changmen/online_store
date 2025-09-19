package com.example.onlinestore.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 更新地址请求DTO
 */
@Data
public class UpdateAddressRequest {
    
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String receiverName;
    
    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
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
     * 区/县
     */
    @NotBlank(message = "区/县不能为空")
    @Size(max = 20, message = "区/县名称长度不能超过20个字符")
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
    private String postCode;
    
    /**
     * 是否为默认地址
     */
    private Boolean isDefault;
}