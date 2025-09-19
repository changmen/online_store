package com.example.onlinestore.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 地址实体类
 * 对应数据库中的address表
 */
@Data
public class AddressEntity {
    
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
     * 联系电话
     */
    private String phone;
    
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
    private String postCode;
    
    /**
     * 是否为默认地址
     */
    private Boolean isDefault;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}