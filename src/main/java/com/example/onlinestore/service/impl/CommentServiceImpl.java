package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Comment;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CommentStatistics;
import com.example.onlinestore.entity.CommentEntity;
import com.example.onlinestore.enums.CommentStatus;
import com.example.onlinestore.enums.CommentType;
import com.example.onlinestore.exception.CommentException;
import com.example.onlinestore.hook.CommentHookPoint;
import com.example.onlinestore.hook.CommentHookManager;
import com.example.onlinestore.mapper.CommentMapper;
import com.example.onlinestore.service.CommentService;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.validator.CommentCountValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private static final BeanCopier COMMENT_TO_ENTITY = BeanCopier.create(Comment.class, CommentEntity.class, false);
    private static final BeanCopier ENTITY_TO_COMMENT = BeanCopier.create(CommentEntity.class, Comment.class, false);
    private static final int MAX_COMMENT_LENGTH = 140;

    private final CommentMapper commentMapper;
    private final CommentHookManager hookManager;
    private final CommentCountValidator commentCountValidator;
    private final ItemService itemService;

    public CommentServiceImpl(CommentMapper commentMapper,
                              CommentHookManager hookManager,
                              CommentCountValidator commentCountValidator,
                              ItemService itemService) {
        this.commentMapper = commentMapper;
        this.hookManager = hookManager;
        this.commentCountValidator = commentCountValidator;
        this.itemService = itemService;
    }

    @Override
    public Long addComment(Comment comment) {
        if (comment == null) {
            throw new CommentException("Comment cannot be null");
        }
        if (comment.getItemId() == null) {
            throw new CommentException("Item ID cannot be null");
        }
        if (comment.getUserId() == null) {
            throw new CommentException("User ID cannot be null");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new CommentException("Comment content cannot be empty");
        }
        if (comment.getRating() == null || comment.getRating() < 1 || comment.getRating() > 5) {
            throw new CommentException("Rating must be between 1 and 5");
        }

        int currentCommentCount = commentMapper.countUserItemComments(
                comment.getUserId(), comment.getItemId());

        if (!commentCountValidator.validateCommentCount(currentCommentCount)) {
            logger.warn("User {} has reached maximum comment limit for item {}",
                    comment.getUserId(), comment.getItemId());
            throw new CommentException(
                    "You have reached the maximum number of comments allowed for this item");
        }

        if (comment.getContent().length() > MAX_COMMENT_LENGTH) {
            logger.warn("Comment content length exceeds limit for item {}", comment.getItemId());
            throw new CommentException("Comment content length exceeds limit");
        }

        if (!hookManager.executeHooks(CommentHookPoint.BEFORE_INSERT, comment)) {
            logger.info("Comment insertion cancelled by before-insert hook");
            return null;
        }

        CommentEntity entity = new CommentEntity();
        COMMENT_TO_ENTITY.copy(comment, entity, null);

        entity.setCreateTime(new Date());
        entity.setStatus(CommentStatus.PENDING);
        entity.setType(CommentType.REGULAR_REVIEW);
        entity.setEmotionalScore(0);
        entity.setIsVerifiedPurchase(false);
        entity.setLanguageCode("en");

        logger.debug("Adding new comment for item: {}, user: {}",
                comment.getItemId(), comment.getUserId());
        commentMapper.insertComment(entity);
        logger.info("Successfully added comment: {}", entity.getId());

        comment.setId(entity.getId());

        hookManager.executeHooks(CommentHookPoint.AFTER_INSERT, comment);

        return entity.getId();
    }

    @Override
    public Comment getComment(Long commentId) {
        if (commentId == null) {
            throw new CommentException("Comment ID cannot be null");
        }

        logger.debug("Retrieving comment: {}", commentId);
        CommentEntity comment = commentMapper.findById(commentId);
        logger.debug("Retrieved comment: {}", comment != null ? comment.getId() : "not found");
        return convertToDTO(comment);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        if (commentId == null) {
            throw new CommentException("Comment ID cannot be null");
        }

        Comment comment = getComment(commentId);
        if (comment == null) {
            return false;
        }

        if (!hookManager.executeHooks(CommentHookPoint.BEFORE_DELETE, comment)) {
            logger.info("Comment deletion cancelled by before-delete hook");
            return false;
        }

        logger.debug("Deleting comment: {}", commentId);
        commentMapper.deleteComment(commentId);
        logger.info("Successfully deleted comment: {}", commentId);

        hookManager.executeHooks(CommentHookPoint.AFTER_DELETE, comment);

        return true;
    }

    @Override
    public List<Comment> getItemComments(Long itemId, int page, int size) {
        if (itemId == null) {
            throw new CommentException("Item ID cannot be null");
        }

        if (page < 1 || size < 1) {
            throw new CommentException("Invalid pagination parameters");
        }

        int offset = (page - 1) * size;

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
            throw new CommentException("Item ID cannot be null");
        }

        return commentMapper.countByItemId(itemId);
    }

    @Override
    public CommentStatistics getItemCommentStatistics(Long itemId) {
        if (itemId == null) {
            throw new CommentException("Item ID cannot be null");
        }

        Item item = itemService.getItemById(itemId);
        if (item == null) {
            throw new CommentException("Item not found");
        }

        Map<String, Object> basicStats = commentMapper.getBasicStatistics(itemId);
        Long totalComments = ((Number) basicStats.get("totalComments")).longValue();
        Double averageRating = (Double) basicStats.get("averageRating");

        List<Map<String, Object>> ratingDist = commentMapper.getRatingDistribution(itemId);
        Map<Integer, Long> ratingCounts = new HashMap<>();
        for (Map<String, Object> rating : ratingDist) {
            int stars = ((Number) rating.get("rating")).intValue();
            long count = ((Number) rating.get("count")).longValue();
            ratingCounts.put(stars, count);
        }

        CommentStatistics.RatingDistribution distribution = new CommentStatistics.RatingDistribution(
            ratingCounts.getOrDefault(5, 0L),
            ratingCounts.getOrDefault(4, 0L),
            ratingCounts.getOrDefault(3, 0L),
            ratingCounts.getOrDefault(2, 0L),
            ratingCounts.getOrDefault(1, 0L)
        );

        Map<String, Object> verificationStats = commentMapper.getVerificationStats(itemId);
        Long verifiedCount = ((Number) verificationStats.get("verifiedCount")).longValue();
        Double verifiedAvgRating = (Double) verificationStats.get("verifiedAvgRating");
        Double unverifiedAvgRating = (Double) verificationStats.get("unverifiedAvgRating");

        Double verifiedPercentage = totalComments > 0 ?
            (verifiedCount.doubleValue() / totalComments) * 100 : 0.0;

        CommentStatistics.VerificationStats verification = new CommentStatistics.VerificationStats(
            verifiedCount,
            verifiedPercentage,
            verifiedAvgRating,
            unverifiedAvgRating
        );

        return new CommentStatistics(
            itemId,
            item.getName(),
            totalComments,
            averageRating,
            distribution,
            verification
        );
    }

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

        List<CommentEntity> entities = commentMapper.findByAdvancedCondition(
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
        ENTITY_TO_COMMENT.copy(entity, dto, null);
        return dto;
    }
}
