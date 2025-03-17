package com.example.onlinestore.dto;

public class CommentStatistics {
    // Basic statistics
    private Long itemId;
    private String itemName;
    private Long totalComments;
    private Double averageRating;
    
    // Rating distribution
    private RatingDistribution ratingDistribution;
    
    // Verification statistics
    private VerificationStats verificationStats;

    // VIP user statistics
    private Long vipUsers;

    public CommentStatistics(Long itemId, String itemName,Long totalComments, Double averageRating,
                            RatingDistribution ratingDistribution, VerificationStats verificationStats) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.totalComments = totalComments;
        this.averageRating = averageRating;
        this.ratingDistribution = ratingDistribution;
        this.verificationStats = verificationStats;
    }

    // Getters and setters
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getTotalComments() { return totalComments; }
    public void setTotalComments(Long totalComments) { this.totalComments = totalComments; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public RatingDistribution getRatingDistribution() { return ratingDistribution; }
    public void setRatingDistribution(RatingDistribution ratingDistribution) { this.ratingDistribution = ratingDistribution; }
    public VerificationStats getVerificationStats() { return verificationStats; }
    public void setVerificationStats(VerificationStats verificationStats) { this.verificationStats = verificationStats; }

    public static class RatingDistribution {
        private Long fiveStarCount;
        private Long fourStarCount;
        private Long threeStarCount;
        private Long twoStarCount;
        private Long oneStarCount;

        public RatingDistribution(Long fiveStarCount, Long fourStarCount, Long threeStarCount,
                                Long twoStarCount, Long oneStarCount) {
            this.fiveStarCount = fiveStarCount;
            this.fourStarCount = fourStarCount;
            this.threeStarCount = threeStarCount;
            this.twoStarCount = twoStarCount;
            this.oneStarCount = oneStarCount;
        }

        // Getters and setters
        public Long getFiveStarCount() { return fiveStarCount; }
        public void setFiveStarCount(Long fiveStarCount) { this.fiveStarCount = fiveStarCount; }
        public Long getFourStarCount() { return fourStarCount; }
        public void setFourStarCount(Long fourStarCount) { this.fourStarCount = fourStarCount; }
        public Long getThreeStarCount() { return threeStarCount; }
        public void setThreeStarCount(Long threeStarCount) { this.threeStarCount = threeStarCount; }
        public Long getTwoStarCount() { return twoStarCount; }
        public void setTwoStarCount(Long twoStarCount) { this.twoStarCount = twoStarCount; }
        public Long getOneStarCount() { return oneStarCount; }
        public void setOneStarCount(Long oneStarCount) { this.oneStarCount = oneStarCount; }
    }
    
    public static class VerificationStats {
        private Long verifiedPurchaseCount;
        private Double verifiedPurchasePercentage;
        private Double averageVerifiedRating;
        private Double averageUnverifiedRating;

        public VerificationStats(Long verifiedPurchaseCount, Double verifiedPurchasePercentage,
                               Double averageVerifiedRating, Double averageUnverifiedRating) {
            this.verifiedPurchaseCount = verifiedPurchaseCount;
            this.verifiedPurchasePercentage = verifiedPurchasePercentage;
            this.averageVerifiedRating = averageVerifiedRating;
            this.averageUnverifiedRating = averageUnverifiedRating;
        }

        // Getters and setters
        public Long getVerifiedPurchaseCount() { return verifiedPurchaseCount; }
        public void setVerifiedPurchaseCount(Long verifiedPurchaseCount) { this.verifiedPurchaseCount = verifiedPurchaseCount; }
        public Double getVerifiedPurchasePercentage() { return verifiedPurchasePercentage; }
        public void setVerifiedPurchasePercentage(Double verifiedPurchasePercentage) { this.verifiedPurchasePercentage = verifiedPurchasePercentage; }
        public Double getAverageVerifiedRating() { return averageVerifiedRating; }
        public void setAverageVerifiedRating(Double averageVerifiedRating) { this.averageVerifiedRating = averageVerifiedRating; }
        public Double getAverageUnverifiedRating() { return averageUnverifiedRating; }
        public void setAverageUnverifiedRating(Double averageUnverifiedRating) { this.averageUnverifiedRating = averageUnverifiedRating; }
    }
}
