package com.example.onlinestore.errors;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum ErrorCode {
    MEMBER_PASSWORD_INCORRECT("ErrorCode.Member.Password.Incorrect", "密码不正确"),
    INTERNAL_SERVER_ERROR("ErrorCode.Internal.Error", "系统内部错误"),
    MEMBER_NOT_FOUND("ErrorCode.Member.NotFound", "会员:{0}不存在"),
    MEMBER_EXISTED("ErrorCode.Member.Existed", "会员:{0}已存在"),
    MEMBER_NOT_LOGIN("ErrorCode.Member.Not.Login", "会员未登录"),
    ITEM_NOT_FOUND("ErrorCode.Item.NotFound", "商品不存在"),
    BRAND_NOT_FOUND("ErrorCode.Brand.NotFound", "品牌不存在"),
    BRAND_NAME_DUPLICATED("ErrorCode.Brand.Name.Duplicated", "品牌名称:{0}已存在"),
    BRAND_NAME_MODIFY_FORBIDDEN("ErrorCode.Brand.Name.Modify.Forbidden", "品牌名称不能修改"),
    BRAND_NAME_CONTAIN_SPECIAL_CHARACTER("ErrorCode.Brand.Name.Contain.Special.Character", "品牌名称不能包含特殊字符"),
    ATTRIBUTE_NAME_DUPLICATED("ErrorCode.Attribute.Name.Duplicated", "属性名称:{0}已存在"),
    ATTRIBUTE_NOT_FOUND("ErrorCode.Attribute.NotFound", "属性不存在"),
    ITEM_NAME_CONTAINS_FORBIDDEN_WORDS("ErrorCode.Item.Name.Contains.Forbidden.Words", "商品名称不能包含以下关键字:{0},请修改后重新提交"),
    ITEM_DESCRIPTION_CONTAINS_FORBIDDEN_WORDS("ErrorCode.Item.Description.Contains.Forbidden.Words", "商品描述不能包含以下关键字:{0}"),
    ATTRIBUTE_VALUE_NOT_FOUND("ErrorCode.Attribute.Value.NotFound", "属性值不存在"),
    ITEM_ATTRIBUTE_VALUE_IS_EMPTY("ErrorCode.Item.Attribute.Value.Is.Empty", "商品属性：{0}值不能为空"),
    REQUEST_OSS_FAILED("ErrorCode.Request.OSS.Failed", "请求OSS失败"),
    SKU_CODE_EXISTS("ErrorCode.Sku.Code.Exists", "商品编码:{0}已存在"),
    SKU_NOT_FOUND("ErrorCode.Sku.NotFound", "商品SKU不存在"),
    SKU_STOCK_INSUFFICIENT("ErrorCode.Sku.Stock.Insufficient", "商品库存不足"),
    CART_ITEM_NOT_FOUND("ErrorCode.Cart.Item.NotFound", "购物车项不存在"),
    ATTRIBUTE_IS_REFERENCE_BY_ITEM("ErrorCode.Attribute.Is.Reference.By.Item", "属性:{0}已被商品引用，不能删除"),
    SKU_WARNING_QUANTITY_EXCEEDS_STOCK_QUANTITY("ErrorCode.SKU.Warning.Quantity.Exceeds.Stock.Quantity", "SKU预警库存数量不能超过库存数量"),
    ATTRIBUTE_TYPE_NOT_SKU("ErrorCode.Attribute.Type.Not.SKU", "属性:{0}类型不是SKU类型"),
    SKU_ATTRIBUTE_INPUT_TYPE_INVALID("ErrorCode.SKU.Attribute.Input.Type.Invalid", "SKU属性:{0}输入类型只能是单选或多选"),
    SKU_ATTRIBUTE_VALUE_EMPTY("ErrorCode.SKU.Attribute.Value.Empty", "SKU属性值:{0}不能为空"),
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