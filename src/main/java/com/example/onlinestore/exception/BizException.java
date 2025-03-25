package com.example.onlinestore.exception;

import com.example.onlinestore.errors.ErrorCode;

import java.text.MessageFormat;

public class BizException extends RuntimeException{
    private String errorCode;
    private String errorMsg;
    private Object[] args;

    public BizException(String errorCode, String errorMsg, Object... args) {
        super(MessageFormat.format("{0}:{1}", errorCode, errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.args = args;
    }

    public BizException(String errorCode, String errorMsg) {
        super(MessageFormat.format("{0}:{1}", errorCode, errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }



    public BizException( String errorCode, String errorMsg, Object[] args,Throwable cause) {
        super(MessageFormat.format("{0}:{1}", errorCode, errorMsg), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.args = args;
    }

    public BizException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object[] getArgs() {
        return args;
    }
}
