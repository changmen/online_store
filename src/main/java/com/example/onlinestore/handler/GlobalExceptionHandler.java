package com.example.onlinestore.handler;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public Response<String> handleException(DataAccessException e) {
        logger.error("Database error", e);
        return Response.failWithInternalError();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Response<String> handleException(RuntimeException e) {
        logger.error("Runtime error", e);
        return Response.failWithInternalError();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response<String> handleException(Exception e) {
        logger.error("Internal server error", e);
        return Response.failWithInternalError();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> handleException(MethodArgumentNotValidException e) {
        logger.warn("Invalid request: {}", e.getMessage());
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (CollectionUtils.isNotEmpty(errors)) {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < errors.size(); i++) {
                    FieldError fieldError = (FieldError) errors.get(i);
                    if (i > 0) {
                        message.append("; ");
                    }
                    message.append(MessageFormat.format("Parameter:{0}, error:{1}", fieldError.getField(), fieldError.getDefaultMessage()));
                }
                return Response.fail(message.toString());
            }
        }
        return Response.fail("Invalid request");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<String> handleException(MissingServletRequestParameterException e) {
        logger.warn("Missing request parameter: {}", e.getParameterName());
        return Response.fail(localize("error.request.param.missing", e.getParameterName()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<String> handleException(HttpMessageNotReadableException e) {
        logger.warn("Message not readable: {}", e.getMessage());
        return Response.fail(localize("error.request.body.unreadable"));
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<String> handleException(HttpRequestMethodNotSupportedException e) {
        logger.warn("Method not allowed: {}", e.getMethod());
        return Response.fail(localize("error.request.method.not.supported", e.getMethod()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public Response<String> handleException(NoResourceFoundException e) {
        return Response.fail(localize("error.resource.not.found"));
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Response<String>> handleException(BizException e) {

        if (e.getErrorCode() == null) {
            logger.error("BizException. errorCode is null", e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Response.fail("INTERNAL ERROR"));
        }

        logger.warn("BizException. errorCode:{}, params:{}", e.getErrorCode(), e.getParams());

        String message;
        try {
            message = messageSource.getMessage(e.getErrorCode().getCode(), null, LocaleContextHolder.getLocale());
            if (StringUtils.isBlank(message)) {
                message = e.getErrorCode().getDefaultMessage();
            }

        } catch (NoSuchMessageException ne) {
            logger.warn("NoSuchMessageException. {}", e.getErrorCode().getCode());
            message = e.getErrorCode().getDefaultMessage();
        }

        if (e.getParams() != null && e.getParams().length > 0) {
            message = MessageFormat.format(message, e.getParams());
        }

        HttpStatus status = resolveHttpStatus(e.getErrorCode());
        return ResponseEntity.status(status).body(Response.fail(message));
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        return errorCode.getHttpStatus();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<String> handleException(ConstraintViolationException e) {
        logger.warn("ConstraintViolationException: {}", e.getMessage());
        StringBuilder message = new StringBuilder(localize("error.request.validation.failed")).append(": ");
        e.getConstraintViolations().forEach(violation ->
                message.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("; "));
        return Response.fail(message.toString());
    }

    private String localize(String code, Object... args) {
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            logger.warn("NoSuchMessageException. {}", code);
            return code;
        }
    }
}
