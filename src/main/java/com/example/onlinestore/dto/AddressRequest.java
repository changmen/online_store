package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddressRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8362960821338683596L;

    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @NotBlank(message = "收货人姓名不能为空")
    @Pattern(regexp = Constants.MEMBER_NAME_PATTERN, message = "收货人姓名格式不正确,应为2-16位的中文、英文或数字")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = Constants.PHONE_PATTERN, message = "收货人电话格式不正确,应为11位有效手机号")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区县不能为空")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;

    private Boolean isDefault = false;
} 