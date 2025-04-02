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
    public static final String MEMBER_PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$";

    /**
     * 手机号规则
     */
    public static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";

    /**
     * URL规则
     */
    public static final String URL_PATTERN = "^(http|https)://.*$";
}
