package com.example.onlinestore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应类
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3007035474253446009L;

    private boolean success;
    private String message;
    private T data;
    
    public Response(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Response<T> success() {
        return new Response<>(true, "操作成功", null);
    }
    
    public static <T> Response<T> success(T data) {
        return new Response<>(true, "操作成功", data);
    }
    
    public static <T> Response<T> fail(String message) {
        return new Response<>(false, message, null);
    }

    public static <T> Response<T> failWithInternalError() {
        return fail("INTERNAL_SERVER_ERROR");
    }

}
