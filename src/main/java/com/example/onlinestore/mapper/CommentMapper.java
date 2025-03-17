package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(CommentEntity comment);
    
    CommentEntity findById(Long id);
    
    void deleteComment(Long id);
    
    List<CommentEntity> findByItemId(@Param("itemId") Long itemId, 
                                    @Param("offset") int offset, 
                                    @Param("limit") int limit);
    
    long countByItemId(@Param("itemId") Long itemId);
    
    // Speculative Generality: 过多的可能用不到的查询方法
    List<CommentEntity> findByAdvancedCondition(
            @Param("itemId") Long itemId,
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("type") String type,
            @Param("minEmotionalScore") Integer minEmotionalScore,
            @Param("isVerifiedPurchase") Boolean isVerifiedPurchase,
            @Param("deviceType") String deviceType,
            @Param("locationInfo") String locationInfo,
            @Param("languageCode") String languageCode,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
    
    // Speculative Generality: 过多的统计方法
    long countByAdvancedCondition(
            @Param("itemId") Long itemId,
            @Param("status") String status,
            @Param("type") String type,
            @Param("isVerifiedPurchase") Boolean isVerifiedPurchase
    );
} 