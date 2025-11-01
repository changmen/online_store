package com.example.onlinestore.bean;

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
import java.time.LocalDateTime;

/**
 * 地址信息业务对象
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Address implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 8765432109876543210L;
    
    /**
     * 地址ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long memberId;
    
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
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 获取完整地址字符串
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) {
            sb.append(province);
        }
        if (city != null) {
            sb.append(city);
        }
        if (district != null) {
            sb.append(district);
        }
        if (detailAddress != null) {
            sb.append(detailAddress);
        }
        return sb.toString();
    }
}