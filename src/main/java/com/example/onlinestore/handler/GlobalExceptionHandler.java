package com.example.onlinestore.handler;

import com.example.onlinestore.dto.ErrorResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.exception.BusinessException;
import com.example.onlinestore.exception.ItemNameInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.warn("Business exception at {}: {}", request.getRequestURI(), e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(ItemNameInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> handleItemNameInvalid(ItemNameInvalidException e, HttpServletRequest request) {
        logger.warn("Invalid item name at {}: {}", request.getRequestURI(), e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        logger.warn("Illegal argument at {}: {}", request.getRequestURI(), e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return Response.fail(message);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<ErrorResponse> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return Response.fail(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> handleConstraintViolation(ConstraintViolationException e) {
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> handleMissingParam(MissingServletRequestParameterException e) {
        return Response.fail("缺少必填参数: " + e.getParameterName());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<String> handleNoResourceFound(NoResourceFoundException e) {
        return Response.fail("接口不存在");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<String> handleException(Exception e, HttpServletRequest request) {
        logger.error("Unexpected error at {}", request.getRequestURI(), e);
        String message = messageSource.getMessage(
                "error.system.internal", null, "服务器内部错误", LocaleContextHolder.getLocale());
        return Response.fail(message);
    }
}
