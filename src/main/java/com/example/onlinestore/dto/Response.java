package com.example.onlinestore.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应类
 */
public class Response<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String message;
    private T data;
    
    public Response() {
    }
    
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
    
    public static <T> Response<T> fail(String message, T data) {
        return new Response<>(false, message, data);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
