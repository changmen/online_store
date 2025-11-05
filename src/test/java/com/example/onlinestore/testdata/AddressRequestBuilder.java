package com.example.onlinestore.testdata;

import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;

/**
 * 地址请求DTO测试数据构建器
 * 提供标准的地址请求测试数据构建功能
 */
public class AddressRequestBuilder {
    
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String postCode;
    private Boolean isDefault;
    
    private AddressRequestBuilder() {
        // 设置默认值
        this.receiverName = "王五";
        this.phone = "13700137000";
        this.province = "广东省";
        this.city = "深圳市";
        this.district = "南山区";
        this.detailAddress = "科技园南区某大厦A座";
        this.postCode = "518000";
        this.isDefault = false;
    }
    
    public static AddressRequestBuilder builder() {
        return new AddressRequestBuilder();
    }
    
    public AddressRequestBuilder receiverName(String receiverName) {
        this.receiverName = receiverName;
        return this;
    }
    
    public AddressRequestBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public AddressRequestBuilder province(String province) {
        this.province = province;
        return this;
    }
    
    public AddressRequestBuilder city(String city) {
        this.city = city;
        return this;
    }
    
    public AddressRequestBuilder district(String district) {
        this.district = district;
        return this;
    }
    
    public AddressRequestBuilder detailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
        return this;
    }
    
    public AddressRequestBuilder postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }
    
    public AddressRequestBuilder isDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }
    
    /**
     * 构建默认地址请求
     */
    public AddressRequestBuilder asDefault() {
        this.isDefault = true;
        return this;
    }
    
    /**
     * 构建非默认地址请求
     */
    public AddressRequestBuilder asNonDefault() {
        this.isDefault = false;
        return this;
    }
    
    /**
     * 构建有效的地址请求（包含所有必填字段）
     */
    public AddressRequestBuilder valid() {
        return this.receiverName("赵六")
                  .phone("13600136000")
                  .province("江苏省")
                  .city("南京市")
                  .district("鼓楼区")
                  .detailAddress("某某路456号");
    }
    
    /**
     * 构建缺少必填字段的无效请求
     */
    public AddressRequestBuilder withMissingReceiverName() {
        this.receiverName = null;
        return this;
    }
    
    /**
     * 构建缺少电话的无效请求
     */
    public AddressRequestBuilder withMissingPhone() {
        this.phone = null;
        return this;
    }
    
    /**
     * 构建无效电话格式的请求
     */
    public AddressRequestBuilder withInvalidPhone() {
        this.phone = "invalid-phone";
        return this;
    }
    
    /**
     * 构建空字符串字段的请求
     */
    public AddressRequestBuilder withEmptyFields() {
        this.receiverName = "";
        this.phone = "";
        this.detailAddress = "";
        return this;
    }
    
    /**
     * 构建超长字段的请求（用于边界值测试）
     */
    public AddressRequestBuilder withMaxLengthFields() {
        this.receiverName = "这是一个用于测试最大长度限制的非常长的收货人姓名字段内容";
        this.detailAddress = "这是一个用于测试详细地址字段最大长度限制的非常详细和冗长的地址描述信息，包含了大量的具体位置描述和标识信息，旨在验证系统对于长地址信息的处理能力和字段长度验证功能的正确性";
        return this;
    }
    
    /**
     * 构建超出长度限制的请求
     */
    public AddressRequestBuilder withOverMaxLengthFields() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longName.append("超长姓名");
        }
        this.receiverName = longName.toString();
        
        StringBuilder longAddress = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            longAddress.append("超长地址信息");
        }
        this.detailAddress = longAddress.toString();
        return this;
    }
    
    public CreateAddressRequest buildCreateRequest() {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setReceiverName(this.receiverName);
        request.setPhone(this.phone);
        request.setProvince(this.province);
        request.setCity(this.city);
        request.setDistrict(this.district);
        request.setDetailAddress(this.detailAddress);
        request.setPostCode(this.postCode);
        request.setIsDefault(this.isDefault);
        return request;
    }
    
    public UpdateAddressRequest buildUpdateRequest() {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setReceiverName(this.receiverName);
        request.setPhone(this.phone);
        request.setProvince(this.province);
        request.setCity(this.city);
        request.setDistrict(this.district);
        request.setDetailAddress(this.detailAddress);
        request.setPostCode(this.postCode);
        request.setIsDefault(this.isDefault);
        return request;
    }
}