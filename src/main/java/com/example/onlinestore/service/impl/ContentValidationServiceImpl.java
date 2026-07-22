package com.example.onlinestore.service.impl;

import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.service.ContentValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContentValidationServiceImpl implements ContentValidationService {

    private final Set<String> forbiddenWords;

    public ContentValidationServiceImpl(@Value("${forbidden-words:刀}") String forbiddenWords) {
        if (StringUtils.isBlank(forbiddenWords)) {
            this.forbiddenWords = Collections.emptySet();
        } else {
            this.forbiddenWords = Arrays.stream(forbiddenWords.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public void validateForbiddenWords(String text, ErrorCode errorCode) {
        if (StringUtils.isBlank(text) || forbiddenWords.isEmpty()) {
            return;
        }
        String lowerText = StringUtils.toRootLowerCase(StringUtils.trim(text));
        if (forbiddenWords.stream().anyMatch(lowerText::contains)) {
            throw new BizException(errorCode, text);
        }
    }
}
