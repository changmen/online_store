package com.example.onlinestore.testdata;

import com.example.onlinestore.entity.AddressEntity;
import java.time.LocalDateTime;

/**
 * AddressEntity测试数据构建器
 * 提供标准的地址实体测试数据构建功能
 */
public class AddressEntityBuilder {
    
    private Long id;
    private Long memberId;
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String postCode;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private AddressEntityBuilder() {
        // 设置默认值
        this.receiverName = "张三";
        this.phone = "13800138000";
        this.province = "北京市";
        this.city = "北京市";
        this.district = "朝阳区";
        this.detailAddress = "某某街道123号";
        this.postCode = "100000";
        this.isDefault = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public static AddressEntityBuilder builder() {
        return new AddressEntityBuilder();
    }
    
    public AddressEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public AddressEntityBuilder memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }
    
    public AddressEntityBuilder receiverName(String receiverName) {
        this.receiverName = receiverName;
        return this;
    }
    
    public AddressEntityBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public AddressEntityBuilder province(String province) {
        this.province = province;
        return this;
    }
    
    public AddressEntityBuilder city(String city) {
        this.city = city;
        return this;
    }
    
    public AddressEntityBuilder district(String district) {
        this.district = district;
        return this;
    }
    
    public AddressEntityBuilder detailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
        return this;
    }
    
    public AddressEntityBuilder postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }
    
    public AddressEntityBuilder isDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }
    
    public AddressEntityBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public AddressEntityBuilder updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    
    /**
     * 构建默认地址实体
     */
    public AddressEntityBuilder asDefault() {
        this.isDefault = true;
        return this;
    }
    
    /**
     * 构建非默认地址实体
     */
    public AddressEntityBuilder asNonDefault() {
        this.isDefault = false;
        return this;
    }
    
    /**
     * 构建有效的地址实体（包含所有必填字段）
     */
    public AddressEntityBuilder valid() {
        return this.receiverName("李四")
                  .phone("13900139000")
                  .province("上海市")
                  .city("上海市")
                  .district("浦东新区")
                  .detailAddress("世纪大道999号");
    }
    
    /**
     * 构建边界值测试地址（最长字段值）
     */
    public AddressEntityBuilder withMaxLengthFields() {
        return this.receiverName("这是一个非常长的收货人姓名用于测试字段长度限制的边界情况")
                  .detailAddress("这是一个非常详细的地址信息，包含了很多具体的描述信息，用于测试详细地址字段的最大长度限制，确保系统能够正确处理长地址信息而不会出现截断或其他异常情况");
    }
    
    /**
     * 构建空字段测试地址
     */
    public AddressEntityBuilder withEmptyFields() {
        return this.receiverName("")
                  .phone("")
                  .detailAddress("");
    }
    
    public AddressEntity build() {
        AddressEntity entity = new AddressEntity();
        entity.setId(this.id);
        entity.setMemberId(this.memberId);
        entity.setReceiverName(this.receiverName);
        entity.setPhone(this.phone);
        entity.setProvince(this.province);
        entity.setCity(this.city);
        entity.setDistrict(this.district);
        entity.setDetailAddress(this.detailAddress);
        entity.setPostCode(this.postCode);
        entity.setIsDefault(this.isDefault);
        entity.setCreatedAt(this.createdAt);
        entity.setUpdatedAt(this.updatedAt);
        return entity;
    }
}