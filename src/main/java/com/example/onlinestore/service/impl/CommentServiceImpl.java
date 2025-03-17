package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Comment;
import com.example.onlinestore.entity.CommentEntity;
import com.example.onlinestore.enums.CommentStatus;
import com.example.onlinestore.enums.CommentType;
import com.example.onlinestore.hook.CommentHookPoint;
import com.example.onlinestore.hook.CommentHookManager;
import com.example.onlinestore.mapper.CommentMapper;
import com.example.onlinestore.service.CommentService;
import com.example.onlinestore.validator.CommentCountValidator;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private CommentHookManager hookManager;
    
    // LazyClass的注入
    @Autowired
    private CommentCountValidator commentCountValidator;

    @Override
    public Long addComment(Comment comment) {
        // 基本参数验证
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (comment.getItemId() == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        if (comment.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        if (comment.getRating() == null || comment.getRating() < 1 || comment.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        try {
            // 检查用户在该商品下的评论数量
            int currentCommentCount = commentMapper.countUserItemComments(
                    comment.getUserId(), comment.getItemId());
            
            // 使用LazyClass进行验证
            if (!commentCountValidator.validateCommentCount(currentCommentCount)) {
                logger.warn("User {} has reached maximum comment limit for item {}", 
                        comment.getUserId(), comment.getItemId());
                throw new IllegalStateException(
                        "You have reached the maximum number of comments allowed for this item");
            }

            // 执行插入前Hook
            if (!hookManager.executeHooks(CommentHookPoint.BEFORE_INSERT, comment)) {
                logger.info("Comment insertion cancelled by before-insert hook");
                return null;
            }

            // 转换为实体对象
            CommentEntity entity = new CommentEntity();
            BeanCopier copier = BeanCopier.create(Comment.class, CommentEntity.class, false);
            copier.copy(comment, entity, null);

            // 设置创建时间
            entity.setCreateTime(new Date());
            
            // Speculative Generality: 设置过多的默认值
            entity.setStatus(CommentStatus.PENDING);
            entity.setType(CommentType.REGULAR_REVIEW);
            entity.setEmotionalScore(0);
            entity.setIsVerifiedPurchase(false);
            entity.setLanguageCode("en");
            
            // 保存评论
            logger.debug("Adding new comment for item: {}, user: {}", 
                    comment.getItemId(), comment.getUserId());
            commentMapper.insertComment(entity);
            logger.info("Successfully added comment: {}", entity.getId());
            
            // 设置ID
            comment.setId(entity.getId());
            
            // 执行插入后Hook
            hookManager.executeHooks(CommentHookPoint.AFTER_INSERT, comment);
            
            return entity.getId();
        } catch (Exception e) {
            logger.error("Failed to add comment for item: {}, user: {}", 
                    comment.getItemId(), comment.getUserId(), e);
            throw new RuntimeException("Failed to add comment", e);
        }
    }

    @Override
    public Comment getComment(Long commentId) {
        if (commentId == null) {
            throw new IllegalArgumentException("Comment ID cannot be null");
        }
        
        // Speculative Generality: 过度的日志记录
        logger.debug("Retrieving comment: {}", commentId);
        CommentEntity comment = commentMapper.findById(commentId);
        logger.debug("Retrieved comment: {}", comment != null ? comment.getId() : "not found");
        return convertToDTO(comment);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        if (commentId == null) {
            throw new IllegalArgumentException("Comment ID cannot be null");
        }
        
        try {
            // 获取评论
            Comment comment = getComment(commentId);
            if (comment == null) {
                return false;
            }

            // 执行删除前Hook
            if (!hookManager.executeHooks(CommentHookPoint.BEFORE_DELETE, comment)) {
                logger.info("Comment deletion cancelled by before-delete hook");
                return false;
            }

            logger.debug("Deleting comment: {}", commentId);
            commentMapper.deleteComment(commentId);
            logger.info("Successfully deleted comment: {}", commentId);
            
            // 执行删除后Hook
            hookManager.executeHooks(CommentHookPoint.AFTER_DELETE, comment);
            
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete comment: {}", commentId, e);
            return false;
        }
    }

    @Override
    public List<Comment> getItemComments(Long itemId, int page, int size) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
        
        int offset = (page - 1) * size;
        
        // Speculative Generality: 过度的日志记录
        logger.debug("Retrieving comments for item: {}, page: {}, size: {}", itemId, page, size);
        List<CommentEntity> comments = commentMapper.findByItemId(itemId, offset, size);
        logger.debug("Retrieved {} comments for item: {}", comments.size(), itemId);
        if (CollectionUtils.isEmpty(comments)) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countItemComments(Long itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        
        return commentMapper.countByItemId(itemId);
    }



    // Speculative Generality: 实现过多的可能用不到的高级功能
    @Override
    public List<Comment> findCommentsByAdvancedCondition(
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
            int size) {
        
        int offset = (page - 1) * size;

        List<CommentEntity> entities =  commentMapper.findByAdvancedCondition(
                itemId,
                userId,
                status != null ? status.name() : null,
                type != null ? type.name() : null,
                minEmotionalScore,
                isVerifiedPurchase,
                deviceType,
                locationInfo,
                languageCode,
                offset,
                size
        );

       if (CollectionUtils.isEmpty(entities)) {
           return Collections.emptyList();
       }

        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Comment convertToDTO(CommentEntity entity) {
        Comment dto = new Comment();
        BeanCopier copier = BeanCopier.create(CommentEntity.class, Comment.class, false);
        copier.copy(entity, dto, null);
        return dto;
    }
} 