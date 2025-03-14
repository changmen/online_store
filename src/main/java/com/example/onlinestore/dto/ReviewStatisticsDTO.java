package com.example.onlinestore.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 商品评价统计DTO
 */
public class ReviewStatisticsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3059086678133745962L;
    // 评价总数
    private int totalCount;
    
    // 好评数（4-5星）
    private int positiveCount;
    
    // 中评数（3星）
    private int neutralCount;
    
    // 差评数（1-2星）
    private int negativeCount;
    
    // 平均评分
    private BigDecimal averageRating;
    
    // 各星级评价数量
    private Map<Integer, Integer> ratingDistribution;
    
    // 有图片的评价数量
    private int withImageCount;
    
    // 有追评的评价数量
    private int withAdditionalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(int positiveCount) {
        this.positiveCount = positiveCount;
    }

    public int getNeutralCount() {
        return neutralCount;
    }

    public void setNeutralCount(int neutralCount) {
        this.neutralCount = neutralCount;
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(int negativeCount) {
        this.negativeCount = negativeCount;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Map<Integer, Integer> getRatingDistribution() {
        return ratingDistribution;
    }

    public void setRatingDistribution(Map<Integer, Integer> ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }

    public int getWithImageCount() {
        return withImageCount;
    }

    public void setWithImageCount(int withImageCount) {
        this.withImageCount = withImageCount;
    }

    public int getWithAdditionalCount() {
        return withAdditionalCount;
    }

    public void setWithAdditionalCount(int withAdditionalCount) {
        this.withAdditionalCount = withAdditionalCount;
    }
} 