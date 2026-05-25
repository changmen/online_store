package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.entity.CategoryEntity;
import com.example.onlinestore.mapper.CategoryMapper;
import com.example.onlinestore.service.CategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private static final Object LOAD_LOCKER = new Object();
    private static final BeanCopier ENTITY_TO_BEAN = BeanCopier.create(CategoryEntity.class, Category.class, false);
    private static final BeanCopier BEAN_TO_ENTITY = BeanCopier.create(Category.class, CategoryEntity.class, false);

    private Set<Long> rootCategories = new HashSet<>();

    private final ScheduledExecutorService scheduleExecutorService = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "category-reloader");
        t.setDaemon(true);
        return t;
    });

    private final Map<Long, Category> categoryMap = new ConcurrentHashMap<>();

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @PostConstruct
    private void init() {
        scheduleExecutorService.scheduleAtFixedRate(this::loadCategory, 0, 1, TimeUnit.MINUTES);
    }

    @PreDestroy
    private void destroy() {
        scheduleExecutorService.shutdownNow();
    }


    @Override
    public boolean isRootCategory(Long categoryId) {
        if (categoryMap.containsKey(categoryId)) {
            return Objects.equals(categoryMap.get(categoryId).getParentId(), Constants.ROOT_CATEGORY_PARENT_ID);
        }
        return false;
    }

    @Override
    public List<Category> getRootCategories() {
        if (!rootCategories.isEmpty()) {
            return rootCategories.stream().map(categoryMap::get).filter(Objects::nonNull).toList();
        }
        return List.of();
    }

    @Override
    @Transactional
    public void addCategory(Category category) {
        LOGGER.info("Adding category: {}", category.getName());

        LocalDateTime now = LocalDateTime.now();

        CategoryEntity entity = new CategoryEntity();
        BEAN_TO_ENTITY.copy(category, entity, null);

        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        categoryMapper.insertCategory(entity);
        category.setId(entity.getId());

        this.loadCategory();

        LOGGER.info("Category added successfully: id={}, name={}", category.getId(), category.getName());
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryMap.get(categoryId);
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        LOGGER.info("Updating category: id={}, name={}", category.getId(), category.getName());

        if (!categoryMap.containsKey(category.getId())) {
            LOGGER.error("Category not found: id={}", category.getId());
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        CategoryEntity entity = new CategoryEntity();
        BEAN_TO_ENTITY.copy(category, entity, null);

        entity.setUpdatedAt(now);

        categoryMapper.updateCategory(entity);

        this.loadCategory();
        LOGGER.info("Category updated successfully: id={}, name={}", category.getId(), category.getName());
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        LOGGER.info("Deleting category: id={}", id);

        synchronized (LOAD_LOCKER) {
            Category category = categoryMap.get(id);
            if (category == null) {
                LOGGER.error("Category not found: id={}", id);
                return;
            }

            if (hasChildren(category)) {
                LOGGER.error("Cannot delete category with children: id={}, children={}", id, category.getChildren());
                return;
            }

            Long parentId = category.getParentId();
            if (parentId != null && !Objects.equals(parentId, Constants.ROOT_CATEGORY_PARENT_ID)) {
                Category parent = categoryMap.get(parentId);
                if (parent != null && parent.getChildren() != null) {
                    parent.getChildren().remove(id);
                }
            }

            if (Objects.equals(parentId, Constants.ROOT_CATEGORY_PARENT_ID)) {
                rootCategories.remove(id);
            }

            categoryMap.remove(id);
            categoryMapper.deleteCategory(id);
        }

        LOGGER.info("Category deleted successfully: id={}", id);
    }

    @Override
    public List<Category> getAllCategories() {
        if (!categoryMap.isEmpty()) {
            return new ArrayList<>(categoryMap.values());
        }
        return List.of();
    }

    @Override
    public List<Category> getChildCategories(Long parentId) {
        if (parentId == null) {
            return List.of();
        }

        Category parent = categoryMap.get(parentId);
        if (parent == null || parent.getChildren() == null || parent.getChildren().isEmpty()) {
            return List.of();
        }

        return parent.getChildren().stream()
                .map(categoryMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void loadCategory() {
        LOGGER.info("Start to load category.");
        synchronized (LOAD_LOCKER) {
            int offset = 1;
            int limit = 1000;
            try {
                List<CategoryEntity> allCategories = categoryMapper.findAllCategories(offset, limit);

                Map<Long, Set<Long>> childrenByParentId = new HashMap<>();
                for (CategoryEntity entity : allCategories) {
                    long parentId = entity.getParentId();
                    childrenByParentId.computeIfAbsent(parentId, k -> new HashSet<>()).add(entity.getId());
                }

                Set<Long> newCategoryIds = new HashSet<>();
                for (CategoryEntity entity : allCategories) {
                    newCategoryIds.add(entity.getId());

                    Category category = new Category();
                    ENTITY_TO_BEAN.copy(entity, category, null);

                    Set<Long> children = childrenByParentId.getOrDefault(entity.getId(), Set.of());
                    category.setChildren(new HashSet<>(children));

                    categoryMap.put(entity.getId(), category);
                }

                Set<Long> newRoots = new HashSet<>();
                Iterator<Map.Entry<Long, Category>> it = categoryMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Long, Category> entry = it.next();
                    long key = entry.getKey();
                    if (newCategoryIds.contains(key)) {
                        Category value = entry.getValue();
                        if (Objects.equals(value.getParentId(), Constants.ROOT_CATEGORY_PARENT_ID)) {
                            newRoots.add(key);
                        }
                    } else {
                        it.remove();
                    }
                }

                rootCategories = newRoots;
            } catch (Throwable t) {
                LOGGER.error("Load category failed", t);
            }
        }

        LOGGER.info("Complete to load category.");
    }

    private boolean hasChildren(Category category) {
        if (category == null) {
            return false;
        }
        return category.getChildren() != null && !category.getChildren().isEmpty();
    }

}

