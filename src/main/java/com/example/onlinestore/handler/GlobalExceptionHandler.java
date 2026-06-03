package com.example.onlinestore.handler;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.exceptions.BizException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.MessageFormat;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public Response<String> handleException(Exception e) {
        logger.error("Internal server error", e);
        return Response.failWithInternalError();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public Response<String> handleException(DataAccessException e) {
        logger.error("Database error", e);
        return Response.failWithInternalError();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> handleException(MethodArgumentNotValidException e) {
        logger.error("Invalid request", e);
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();

            if (CollectionUtils.isNotEmpty(errors)) {
                FieldError fieldError = (FieldError) errors.get(0);
                return Response.fail(MessageFormat.format("Parameter:{0}, error:{1}", fieldError.getField(), fieldError.getDefaultMessage()));
            }

        }
        return Response.fail("Invalid request");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<String> handleException(MissingServletRequestParameterException e) {
        logger.warn("Missing request parameter: {}", e.getParameterName());
        return Response.fail(MessageFormat.format("缺少必填参数: {0}", e.getParameterName()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<String> handleException(HttpMessageNotReadableException e) {
        logger.warn("Message not readable: {}", e.getMessage());
        return Response.fail("请求体格式错误，请检查JSON格式");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<String> handleException(HttpRequestMethodNotSupportedException e) {
        logger.warn("Method not allowed: {}", e.getMethod());
        return Response.fail(MessageFormat.format("不支持的请求方法: {0}", e.getMethod()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public Response<String> handleException(NoResourceFoundException e) {
        return Response.fail("请求的资源不存在");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BizException.class)
    public Response<String> handleException(BizException e) {

        if (e.getErrorCode() == null) {
            logger.error("BizException. errorCode is null", e);
            return Response.fail("INTERNAL ERROR");
        }

        logger.error("BizException. errorCode:{}, params:{}", e.getErrorCode(), e.getParams(), e);

        String message;
        try {
            message = messageSource.getMessage(e.getErrorCode().getCode(), null, LocaleContextHolder.getLocale());
            if (StringUtils.isBlank(message)) {
                message = e.getErrorCode().getDefaultMessage();
            }

        } catch (NoSuchMessageException ne) {
            logger.error("NoSuchMessageException. {}", e.getErrorCode().getCode());
            message = e.getErrorCode().getDefaultMessage();
        }

        if (e.getParams() != null && e.getParams().length > 0) {
            message = MessageFormat.format(message, e.getParams());
        }
        return Response.fail(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<String> handleException(ConstraintViolationException e) {
        logger.error("ConstraintViolationException", e);
        StringBuilder message = new StringBuilder("参数验证失败: ");
        e.getConstraintViolations().forEach(violation ->
                message.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("; "));
        return Response.fail(message.toString());
    }
}
