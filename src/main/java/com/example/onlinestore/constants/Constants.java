package com.example.onlinestore.constants;

public class Constants {
    public static final Long ROOT_CATEGORY_PARENT_ID = 0L;
    /**
     * 会员名规则
     */
    public static final String MEMBER_NAME_PATTERN = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,16}$";

    /**
     * 密码规则
     */
    public static final String MEMBER_PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[a-zA-Z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{8,16}$";

    /**
     * 手机号规则
     */
    public static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";

    /**
     * URL规则
     */
    public static final String URL_PATTERN = "^(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";

    /**
     * 品牌排序字段名规则
     */
    public static final String BRAND_ORDERBY_FIELD_PATTERN = "^[a-zA-Z0-9_]+$";
}
