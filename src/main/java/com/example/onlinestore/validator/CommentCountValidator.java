package com.example.onlinestore.validator;

import org.springframework.stereotype.Component;

/**
 * 评论数量校验
 */
@Component
public class CommentCountValidator {
    
    private static final int MAX_COMMENTS_PER_ITEM = 5;
    
    /**
     * 验证用户在某个商品下的评论数是否超过限制
     */
    public boolean validateCommentCount(int currentCount) {
        return currentCount < MAX_COMMENTS_PER_ITEM;
    }
} 