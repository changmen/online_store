package com.example.onlinestore.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 更新地址请求DTO
 */
@Data
public class UpdateAddressRequest {

    @NotNull(message = "地址ID不能为空")
    private Long id;

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String consigneeName;

    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String consigneePhone;

    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份长度不能超过20个字符")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市长度不能超过20个字符")
    private String city;

    @NotBlank(message = "区县不能为空")
    @Size(max = 30, message = "区县长度不能超过30个字符")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200个字符")
    private String detailAddress;

    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确")
    private String postalCode;

    private Boolean isDefault = false;

    @Size(max = 20, message = "地址标签长度不能超过20个字符")
    private String tag;
}