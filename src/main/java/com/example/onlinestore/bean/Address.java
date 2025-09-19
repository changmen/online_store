package com.example.onlinestore.bean;

import com.example.onlinestore.constants.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 地址信息Bean类
 * 用于业务层的地址数据传递
 */
@Data
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 收货人姓名
     */
    @NotNull(message = "收货人姓名不能为空")
    @Size(min = 2, max = 16, message = "收货人姓名长度应为2-16位")
    private String receiverName;

    /**
     * 收货人手机号
     */
    @NotNull(message = "收货人手机号不能为空")
    @Pattern(regexp = Constants.PHONE_PATTERN, message = "手机号码格式不正确,应为11位有效手机号")
    private String receiverPhone;

    /**
     * 省份代码
     */
    @NotNull(message = "省份代码不能为空")
    private String provinceCode;

    /**
     * 省份名称
     */
    @NotNull(message = "省份名称不能为空")
    private String provinceName;

    /**
     * 城市代码
     */
    @NotNull(message = "城市代码不能为空")
    private String cityCode;

    /**
     * 城市名称
     */
    @NotNull(message = "城市名称不能为空")
    private String cityName;

    /**
     * 区县代码
     */
    @NotNull(message = "区县代码不能为空")
    private String districtCode;

    /**
     * 区县名称
     */
    @NotNull(message = "区县名称不能为空")
    private String districtName;

    /**
     * 详细地址
     */
    @NotNull(message = "详细地址不能为空")
    @Size(min = 5, max = 200, message = "详细地址长度应为5-200位")
    private String detailAddress;

    /**
     * 邮政编码
     */
    @Pattern(regexp = "^[0-9]{6}$", message = "邮政编码应为6位数字")
    private String postalCode;

    /**
     * 是否为默认地址
     */
    private Boolean isDefault;

    /**
     * 地址标签
     */
    @Size(max = 10, message = "地址标签长度不能超过10位")
    private String tag;
}