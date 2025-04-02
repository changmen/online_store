package com.example.onlinestore.utils;

import java.util.Objects;
import java.util.function.Consumer;

public class CommonUtils {
    /**
     * 更新字段值如果发生变更
     *
     * 比较新旧值，当新值非空且与旧值不同时，调用setter方法更新字段值并返回更新状态
     *
     * @param <T> 值类型
     * @param newValue 要设置的新值, 不允许为null，如果是null表示不更新
     * @param oldValue 当前存储的旧值（用于比较）
     * @param setter 实际执行字段更新的方法引用或lambda表达式
     * @return true表示执行了字段更新，false表示未执行更新
     */
    public static <T> boolean updateFieldIfChanged(T newValue, T oldValue, Consumer<T> setter) {
        // 当新值有效且与旧值不同时执行更新
        if (newValue != null && Objects.equals(newValue, oldValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
