package com.example.onlinestore.service;

import com.example.onlinestore.bean.Comment;
import com.example.onlinestore.dto.CommentStatistics;
import com.example.onlinestore.entity.CommentEntity;
import com.example.onlinestore.enums.CommentStatus;
import com.example.onlinestore.enums.CommentType;

import java.util.List;

public interface CommentService {
    /**
     * 添加商品评论
     */
    Long addComment(Comment comment);
    
    /**
     * 获取评论详情
     */
    Comment getComment(Long commentId);
    
    /**
     * 删除评论
     */
    boolean deleteComment(Long commentId);
    
    /**
     * 获取商品评论列表
     */
    List<Comment> getItemComments(Long itemId, int page, int size);
    
    /**
     * 获取商品评论总数
     */
    long countItemComments(Long itemId);
    
    /**
     * 获取商品评论统计信息
     * @param itemId 商品ID
     * @return 评论统计信息，包含评分分布、验证购买统计等
     */
    CommentStatistics getItemCommentStatistics(Long itemId);

    List<Comment> findCommentsByAdvancedCondition(
            Long itemId,
            Long userId,
            CommentStatus status,
            CommentType type,
            Integer minEmotionalScore,
            Boolean isVerifiedPurchase,
            String deviceType,
            String locationInfo,
            String languageCode,
            int page,
            int size);

}