package com.example.onlinestore.validator;

import org.springframework.stereotype.Component;

/**
 * LazyClass: 这个类职责过于简单，完全可以将其逻辑并入CommentService
 * 它只是一个简单的封装，没有真正的业务逻辑
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