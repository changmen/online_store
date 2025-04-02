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
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> handleException(MethodArgumentNotValidException e) {
        logger.error("Invalid request", e);
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();

            // 选第一个参数错误，进行返回
            if (CollectionUtils.isNotEmpty(errors)) {
                FieldError fieldError = (FieldError) errors.get(0);
                return Response.fail(MessageFormat.format("Parameter:{0}, error:{1}", fieldError.getField(), fieldError.getDefaultMessage()));
            }

        }
        return Response.fail("Invalid request");
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
