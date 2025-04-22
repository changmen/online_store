package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Address;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class AddressResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -3959677014104912863L;

    /**
     * 用户地址的唯一标识
     */
    private Long id;

    /**
     * 关联的会员或用户ID
     */
    private Long memberId;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人联系电话
     */
    private String receiverPhone;

    /**
     * 省级行政区名称
     */
    private String province;

    /**
     * 市级行政区名称
     */
    private String city;

    /**
     * 区/县级行政区名称
     */
    private String district;

    /**
     * 详细地址信息（如街道、门牌号等）
     */
    private String detailAddress;

    /**
     * 是否为默认地址标识（true为默认地址）
     */
    private Boolean isDefault;

    public static AddressResponse of(Address address) {
        if (address == null) {
            return null;
        }
        return AddressResponse.builder()
                .id(address.getId())
                .memberId(address.getMemberId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .detailAddress(address.getDetailAddress())
                .isDefault(address.getIsDefault())
                .build();
    }
} 