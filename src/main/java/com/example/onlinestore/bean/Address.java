package com.example.onlinestore.bean;

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

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567890123456789L;

    /**
     * 地址唯一标识符
     */
    private Long id;

    /**
     * 关联的会员ID
     */
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    /**
     * 收货人姓名
     */
    @NotNull(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String receiverName;

    /**
     * 收货人手机号
     */
    @NotNull(message = "收货人手机号不能为空")
    @Pattern(regexp = Constants.PHONE_PATTERN, message = "手机号码格式不正确")
    private String receiverPhone;

    /**
     * 省份
     */
    @NotNull(message = "省份不能为空")
    @Size(max = 20, message = "省份名称长度不能超过20个字符")
    private String province;

    /**
     * 城市
     */
    @NotNull(message = "城市不能为空")
    @Size(max = 20, message = "城市名称长度不能超过20个字符")
    private String city;

    /**
     * 区/县
     */
    @NotNull(message = "区/县不能为空")
    @Size(max = 20, message = "区/县名称长度不能超过20个字符")
    private String district;

    /**
     * 详细地址
     */
    @NotNull(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200个字符")
    private String detailAddress;

    /**
     * 邮政编码
     */
    @Pattern(regexp = "^[1-9]\\d{5}$", message = "邮政编码格式不正确")
    private String postalCode;

    /**
     * 是否为默认地址
     */
    private Boolean isDefault = false;

    /**
     * 地址标签(如:家、公司等)
     */
    @Size(max = 20, message = "地址标签长度不能超过20个字符")
    private String tag;
}