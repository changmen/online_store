package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品详情DTO，包含商品详情页面所需的所有信息
 */
public class ItemDetailDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1202588961888700904L;

    // 商品基本信息
    private Item item;
    
    // 商品所属类目
    private CategoryDTO category;
    
    // 商品SKU列表
    private List<Sku> skus;
    
    // 当前选中的SKU
    private Sku selectedSku;
    
    // 商品规格信息（如颜色、尺寸等）
    private Map<String, List<String>> specifications;
    
    // 商品图片列表
    private List<String> images;
    
    // 商品详情描述（富文本）
    private String detailContent;
    
    // 商品评价统计
    private ReviewStatisticsDTO reviewStatistics;
    
    // 相关推荐商品
    private List<Item> recommendedItems;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public Sku getSelectedSku() {
        return selectedSku;
    }

    public void setSelectedSku(Sku selectedSku) {
        this.selectedSku = selectedSku;
    }

    public Map<String, List<String>> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, List<String>> specifications) {
        this.specifications = specifications;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    public ReviewStatisticsDTO getReviewStatistics() {
        return reviewStatistics;
    }

    public void setReviewStatistics(ReviewStatisticsDTO reviewStatistics) {
        this.reviewStatistics = reviewStatistics;
    }

    public List<Item> getRecommendedItems() {
        return recommendedItems;
    }

    public void setRecommendedItems(List<Item> recommendedItems) {
        this.recommendedItems = recommendedItems;
    }
} 