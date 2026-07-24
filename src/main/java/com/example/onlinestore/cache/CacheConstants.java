package com.example.onlinestore.cache;

public final class CacheConstants {

    private CacheConstants() {
    }

    // Redis 商品详情缓存
    public static final String ITEM_DETAIL_KEY_PREFIX = "ITEM_DETAIL:";
    public static final String ITEM_DETAIL_NULL_VALUE = "NULL";
    public static final long ITEM_DETAIL_TTL_MINUTES = 30;
    public static final long ITEM_DETAIL_NULL_TTL_MINUTES = 5;

    // 本地 Caffeine 缓存 TTL / 容量
    public static final long MEMBER_TTL_MINUTES = 30;
    public static final long MEMBER_NOT_FOUND_TTL_MINUTES = 5;
    public static final long BRAND_TTL_MINUTES = 10;
    public static final long CATEGORY_TTL_MINUTES = 30;
    public static final long ACCESS_COUNT_TTL_SECONDS = 60;
    public static final long HOT_ITEMS_TTL_MINUTES = 2;

    public static final long ATTRIBUTE_TTL_MINUTES = 30;
    public static final long ATTRIBUTE_MAX_SIZE = 2000;
    public static final long ATTRIBUTE_VALUE_MAX_SIZE = 10000;
    public static final long ATTRIBUTE_VALUES_BY_ID_MAX_SIZE = 2000;

    public static String itemDetailKey(Long itemId) {
        return ITEM_DETAIL_KEY_PREFIX + itemId;
    }
}
