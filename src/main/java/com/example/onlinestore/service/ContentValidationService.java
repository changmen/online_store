package com.example.onlinestore.service;

import com.example.onlinestore.errors.ErrorCode;

public interface ContentValidationService {

    void validateForbiddenWords(String text, ErrorCode errorCode);
}
