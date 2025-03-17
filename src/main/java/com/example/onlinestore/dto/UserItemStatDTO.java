package com.example.onlinestore.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 用户商品统计DTO
 */
public class UserItemStatDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private int viewCount;
    private int purchaseCount;
    private List<Long> recentViewedItemIds;
    private List<Long> recentPurchasedItemIds;
    private long totalSpent;
    
    public UserItemStatDTO() {
    }
    
    public UserItemStatDTO(String userId, int viewCount, int purchaseCount, 
                          List<Long> recentViewedItemIds, List<Long> recentPurchasedItemIds,
                          long totalSpent) {
        this.userId = userId;
        this.viewCount = viewCount;
        this.purchaseCount = purchaseCount;
        this.recentViewedItemIds = recentViewedItemIds;
        this.recentPurchasedItemIds = recentPurchasedItemIds;
        this.totalSpent = totalSpent;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public int getPurchaseCount() {
        return purchaseCount;
    }
    
    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
    
    public List<Long> getRecentViewedItemIds() {
        return recentViewedItemIds;
    }
    
    public void setRecentViewedItemIds(List<Long> recentViewedItemIds) {
        this.recentViewedItemIds = recentViewedItemIds;
    }
    
    public List<Long> getRecentPurchasedItemIds() {
        return recentPurchasedItemIds;
    }
    
    public void setRecentPurchasedItemIds(List<Long> recentPurchasedItemIds) {
        this.recentPurchasedItemIds = recentPurchasedItemIds;
    }
    
    public long getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(long totalSpent) {
        this.totalSpent = totalSpent;
    }
} 