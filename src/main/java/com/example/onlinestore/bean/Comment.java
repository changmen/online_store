package com.example.onlinestore.bean;

import com.example.onlinestore.enums.CommentStatus;
import com.example.onlinestore.enums.CommentType;

import java.util.Date;

public class Comment {
    private Long id;
    private Long itemId;
    private Long userId;
    private String content;
    private Integer rating;
    private Date createTime;
    private CommentStatus status;
    private CommentType type;
    
    // Speculative Generality: 过度设计的字段
    private Integer emotionalScore;
    private Boolean isVerifiedPurchase;
    private String languageCode;
    private String deviceInfo;
    private String locationInfo;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public Boolean getVerifiedPurchase() {
        return isVerifiedPurchase;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        isVerifiedPurchase = verifiedPurchase;
    }

    public Integer getEmotionalScore() {
        return emotionalScore;
    }

    public void setEmotionalScore(Integer emotionalScore) {
        this.emotionalScore = emotionalScore;
    }

    public Boolean getIsVerifiedPurchase() {
        return isVerifiedPurchase;
    }

    public void setIsVerifiedPurchase(Boolean verifiedPurchase) {
        isVerifiedPurchase = verifiedPurchase;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
} 