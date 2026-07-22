package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.entity.CategoryEntity;
import com.example.onlinestore.mapper.CategoryMapper;
import com.example.onlinestore.service.CategoryService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private volatile Set<Long> rootCategories = new HashSet<>();

    private final Cache<Long, Category> categoryCache;

    private final CategoryMapper categoryMapper;

    @Override
    public boolean isRootCategory(Long categoryId) {
        Category category = categoryCache.getIfPresent(categoryId);
        if (category != null) {
            return Objects.equals(category.getParentId(), Constants.ROOT_CATEGORY_PARENT_ID);
        }
        return false;
    }

    @Override
    public List<Category> getRootCategories() {
        if (!rootCategories.isEmpty()) {
            return rootCategories.stream()
                    .map(categoryCache::getIfPresent)
                    .filter(Objects::nonNull)
                    .toList();
        }
        return List.of();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryCache.getIfPresent(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryCache.asMap().values());
    }

    @Override
    public List<Category> getChildCategories(Long parentId) {
        Category parent = categoryCache.getIfPresent(parentId);
        if (parent == null || parent.getChildren() == null) {
            return List.of();
        }
        return parent.getChildren().stream()
                .map(categoryCache::getIfPresent)
                .filter(Objects::nonNull)
                .toList();
    }

    @Scheduled(fixedRate = 60000)
    public void loadCategory() {
        LOGGER.info("Start to load category.");
        try {
            List<CategoryEntity> allCategories = categoryMapper.findAll();

            Map<Long, Set<Long>> childrenByParentId = new HashMap<>();
            for (CategoryEntity categoryEntity : allCategories) {
                childrenByParentId
                        .computeIfAbsent(categoryEntity.getParentId(), k -> new HashSet<>())
                        .add(categoryEntity.getId());
            }

            Set<Long> newCategoryIds = new HashSet<>();
            Set<Long> newRoots = new HashSet<>();

            for (CategoryEntity categoryEntity : allCategories) {
                Long id = categoryEntity.getId();
                newCategoryIds.add(id);

                Category category = convertToCategory(categoryEntity);
                category.setChildren(childrenByParentId.getOrDefault(id, Collections.emptySet()));
                categoryCache.put(id, category);

                if (Objects.equals(category.getParentId(), Constants.ROOT_CATEGORY_PARENT_ID)) {
                    newRoots.add(id);
                }
            }

            Set<Long> currentIds = new HashSet<>(categoryCache.asMap().keySet());
            for (Long staleId : currentIds) {
                if (!newCategoryIds.contains(staleId)) {
                    categoryCache.invalidate(staleId);
                }
            }

            rootCategories = newRoots;
        } catch (Throwable t) {
            LOGGER.error("Load category failed", t);
        }
        LOGGER.info("Complete to load category.");
    }

    private Category convertToCategory(CategoryEntity entity) {
        Category category = new Category();
        category.setId(entity.getId());
        category.setName(entity.getName());
        category.setDescription(entity.getDescription());
        category.setParentId(entity.getParentId());
        category.setVisible(entity.getVisible());
        category.setWeight(entity.getWeight());
        return category;
    }
}
