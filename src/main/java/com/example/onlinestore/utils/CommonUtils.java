package com.example.onlinestore.utils;

import java.util.Objects;
import java.util.function.Consumer;

public class CommonUtils {
    /**
     * Updates a field using the provided setter if the new value is non-null and different from the old value.
     *
     * Compares the new value to the old value using null-safe equality. If the new value is not null and not equal to the old value, the setter is invoked with the new value and the method returns {@code true}. Otherwise, no update occurs and {@code false} is returned.
     *
     * @param <T> the type of the value being compared and set
     * @param newValue the new value to set; if {@code null}, no update is performed
     * @param oldValue the current value to compare against
     * @param setter a function that sets the field to the new value
     * @return {@code true} if the field was updated; {@code false} otherwise
     */
    public static <T> boolean updateFieldIfChanged(T newValue, T oldValue, Consumer<T> setter) {
        // 当新值有效且与旧值不同时执行更新
        if (newValue != null && !Objects.equals(newValue, oldValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
