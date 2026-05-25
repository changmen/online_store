package com.example.onlinestore.exception;

public class CommentException extends BusinessException {
    public CommentException(String message) {
        super("COMMENT_ERROR", message);
    }
}
