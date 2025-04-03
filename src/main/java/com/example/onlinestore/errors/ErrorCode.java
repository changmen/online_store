package com.example.onlinestore.errors;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum ErrorCode {
    INTERNAL_ERROR("ErrorCode.Internal.Error", "系统内部错误"),
    MEMBER_NOT_FOUND("ErrorCode.Member.NotFound", "会员:{0}不存在"),
    MEMBER_EXISTED("ErrorCode.Member.Existed", "会员:{0}已存在"),
    ITEM_NOT_FOUND("ErrorCode.Item.NotFound", "商品不存在"),
    BRAND_NOT_FOUND("ErrorCode.Brand.NotFound", "品牌不存在"),
    BRAND_NAME_DUPLICATED("ErrorCode.Brand.Name.Duplicated", "品牌名称:{0}已存在"),
    BRAND_NAME_MODIFY_FORBIDDEN("ErrorCode.Brand.Name.Modify.Forbidden", "品牌名称不能修改"),
    ATTRIBUTE_NAME_DUPLICATED("ErrorCode.Attribute.Name.Duplicated", "属性名称:{0}已存在"),
    ATTRIBUTE_NOT_FOUND("ErrorCode.Attribute.NotFound", "属性不存在"),
    ITEM_NAME_CONTAINS_FORBIDDEN_WORDS("ErrorCode.Item.Name.Contains.Forbidden.Words", "商品名称不能包含以下关键字:{0}"),
    ITEM_DESCRIPTION_CONTAINS_FORBIDDEN_WORDS("ErrorCode.Item.Description.Contains.Forbidden.Words", "商品描述不能包含以下关键字:{0}"),
    ATTRIBUTE_VALUE_NOT_FOUND("ErrorCode.Attribute.Value.NotFound", "属性值不存在"),
    ITEM_ATTRIBUTE_VALUE_IS_EMPTY("ErrorCode.Item.Attribute.Value.Is.Empty", "商品属性：{}值不能为空"),
    REQUEST_OSS_FAILED("ErrorCode.Request.Oss.Failed", "请求OSS失败"),
    ;
    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    // 错误码
    private final String code;

    // 默认错误信息, 支持MessageFormat.format()方式的参数替换
    private final String defaultMessage;

    @Override
    public String toString() {
        return code + ": " + defaultMessage;
    }

    // 判断是否是当前错误码
    public boolean Is(ErrorCode errorCode) {
        return StringUtils.equals(this.code, errorCode.code);
    }
}