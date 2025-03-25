package com.example.onlinestore.errors;

public enum ErrorCode {
    ITEM_NAME_NULL("ErrorCode.ItemName.IsNull", "商品名称不能为空"),
    ITEM_NAME_MAX_LENGTH_EXCEED("ErrorCode.ItemName.MaxLength.Exceed", "商品名称不能超过64个字符"),
    ITEM_NAME_CONTAIN_INVALID_CHAR("ErrorCode.ItemName.Contain.InvalidChar", "商品名称不能包含特殊字符"),
    ITEM_NOT_FOUND("ErrorCode.Item.NotFound", "商品不存在"),
    ;

    private ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
