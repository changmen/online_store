package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
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
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateAddressRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567890123456787L;
    
    @NotNull(message = "收货人姓名不能为空")
    @Size(min = 2, max = 20, message = "收货人姓名长度必须在2-20个字符之间")
    private String receiverName;
    
    @NotNull(message = "收货人手机号不能为空")
    @Pattern(regexp = Constants.PHONE_PATTERN, message = "手机号码格式不正确,应为11位有效手机号")
    private String receiverPhone;
    
    @NotNull(message = "省份不能为空")
    @Size(min = 2, max = 20, message = "省份名称长度必须在2-20个字符之间")
    private String province;
    
    @NotNull(message = "城市不能为空")
    @Size(min = 2, max = 20, message = "城市名称长度必须在2-20个字符之间")
    private String city;
    
    @NotNull(message = "区/县不能为空")
    @Size(min = 2, max = 20, message = "区/县名称长度必须在2-20个字符之间")
    private String district;
    
    @NotNull(message = "详细地址不能为空")
    @Size(min = 5, max = 100, message = "详细地址长度必须在5-100个字符之间")
    private String detailAddress;
    
    @Pattern(regexp = "\\d{6}", message = "邮政编码格式不正确，应为6位数字")
    private String zipCode;
    
    private Boolean isDefault = false;
}