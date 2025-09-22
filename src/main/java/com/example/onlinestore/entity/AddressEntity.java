package com.example.onlinestore.entity;

import com.example.onlinestore.bean.Address;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AddressEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -2345678901234567890L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的会员ID
     */
    private Long memberId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人手机号
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
     * 区/县
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
     * 是否为默认地址(0:否, 1:是)
     */
    private Boolean isDefault;

    /**
     * 地址标签
     */
    private String tag;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 转换为Address Bean对象
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
        address.setIsDefault(this.isDefault);
        address.setTag(this.tag);
        return address;
    }

    /**
     * 从Address Bean对象创建Entity
     */
    public static AddressEntity fromAddress(Address address) {
        AddressEntity entity = new AddressEntity();
        entity.setId(address.getId());
        entity.setMemberId(address.getMemberId());
        entity.setReceiverName(address.getReceiverName());
        entity.setReceiverPhone(address.getReceiverPhone());
        entity.setProvince(address.getProvince());
        entity.setCity(address.getCity());
        entity.setDistrict(address.getDistrict());
        entity.setDetailAddress(address.getDetailAddress());
        entity.setPostalCode(address.getPostalCode());
        entity.setIsDefault(address.getIsDefault());
        entity.setTag(address.getTag());
        return entity;
    }
}