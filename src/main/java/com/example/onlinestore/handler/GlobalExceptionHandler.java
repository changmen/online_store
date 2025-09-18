package com.example.onlinestore.handler;

import com.example.onlinestore.dto.ErrorResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * 处理业务异常
     */
    @ExceptionHandler({ItemInvalidException.class, ItemNameInvalidException.class, 
                      CartException.class, InventoryException.class, OrderException.class})
    public ResponseEntity<Response<String>> handleBusinessException(RuntimeException e) {
        logger.warn("业务异常: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Response.fail(e.getMessage()));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<List<ErrorResponse>>> handleValidationException(MethodArgumentNotValidException e) {
        logger.warn("参数校验失败: {}", e.getMessage());
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ErrorResponse error = new ErrorResponse();
            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            error.setCode("VALIDATION_ERROR");
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(Response.fail("参数校验失败", errors));
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response<List<ErrorResponse>>> handleBindException(BindException e) {
        logger.warn("参数绑定失败: {}", e.getMessage());
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ErrorResponse error = new ErrorResponse();
            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            error.setCode("BIND_ERROR");
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(Response.fail("参数绑定失败", errors));
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<List<ErrorResponse>>> handleConstraintViolationException(ConstraintViolationException e) {
        logger.warn("约束违反: {}", e.getMessage());
        List<ErrorResponse> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            ErrorResponse error = new ErrorResponse();
            error.setField(violation.getPropertyPath().toString());
            error.setMessage(violation.getMessage());
            error.setCode("CONSTRAINT_VIOLATION");
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(Response.fail("约束违反", errors));
    }

    /**
     * 处理参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<String>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.warn("参数类型转换异常: {}", e.getMessage());
        String message = String.format("参数 '%s' 的值 '%s' 无法转换为 %s 类型", 
                                     e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        return ResponseEntity.badRequest().body(Response.fail(message));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("非法参数异常: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Response.fail(e.getMessage()));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Response<String>> handleNullPointerException(NullPointerException e) {
        logger.error("空指针异常: ", e);
        String message = messageSource.getMessage("error.system.internal", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.fail(message));
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleException(Exception e) {
        logger.error("系统异常: ", e);
        String message = messageSource.getMessage("error.system.internal", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.fail(message));
    }
}
