package com.example.onlinestore.hook.impl;

import com.example.onlinestore.bean.Comment;
import com.example.onlinestore.hook.CommentHook;
import com.example.onlinestore.hook.CommentHookPoint;
import org.springframework.stereotype.Component;

@Component
public class CommentValidationHook implements CommentHook {
    @Override
    public int getOrder() {
        return 0; // 最高优先级
    }

    @Override
    public CommentHookPoint getPoint() {
        return CommentHookPoint.BEFORE_INSERT;
    }

    @Override
    public boolean execute(Comment comment) {
        // 内容敏感词检查
        if (containsSensitiveWords(comment.getContent())) {
            return false;
        }
        return true;
    }

    private boolean containsSensitiveWords(String content) {
        // 实现敏感词检查逻辑
        return false;
    }
} 