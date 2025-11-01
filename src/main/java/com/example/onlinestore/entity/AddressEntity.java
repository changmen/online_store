package com.example.onlinestore.entity;

import com.example.onlinestore.bean.Address;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址实体类
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AddressEntity implements Serializable {
    
    @Serial
    private static final long serialVersionUID = -2438567891234567890L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long memberId;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区县
     */
    private String district;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 是否为默认地址 (0: 否, 1: 是)
     */
    private Integer isDefault;
    
    /**
     * 地址标签 (如：家、公司、学校等)
     */
    private String addressLabel;
    
    /**
     * 删除标记 (0: 未删除, 1: 已删除)
     */
    private Integer deleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 转换为 Address Bean
     */
    public Address toAddress() {
        Address address = new Address();
        address.setId(this.id);
        address.setMemberId(this.memberId);
        address.setReceiverName(this.receiverName);
        address.setReceiverPhone(this.receiverPhone);
        address.setProvince(this.province);
        address.setCity(this.city);
        address.setDistrict(this.district);
        address.setDetailAddress(this.detailAddress);
        address.setPostalCode(this.postalCode);
        address.setIsDefault(this.isDefault == 1);
        address.setAddressLabel(this.addressLabel);
        address.setCreatedAt(this.createdAt);
        address.setUpdatedAt(this.updatedAt);
        return address;
    }
}