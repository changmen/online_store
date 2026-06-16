package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.entity.CategoryEntity;
import com.example.onlinestore.mapper.CategoryMapper;
import com.example.onlinestore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import net.sf.cglib.beans.BeanCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService, InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private static final Object LOAD_LOCKER = new Object();
    //一级类目列表
    private Set<Long> rootCategories = new HashSet<>();

    private final ScheduledExecutorService scheduleExecutorService = Executors.newScheduledThreadPool(1);

    private final Map<Long, Category> categoryMap = new ConcurrentHashMap<>();

    private final CategoryMapper categoryMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleExecutorService.scheduleAtFixedRate(this::loadCategory, 0, 1, java.util.concurrent.TimeUnit.MINUTES);

    }
    @Override
    public void destroy() throws Exception {
        scheduleExecutorService.shutdown();
    }


    @Override
    public boolean isRootCategory(Long categoryId) {
        Category category = categoryMap.get(categoryId);
        if (category != null) {
            return Objects.equals(category.getParentId(), Constants.ROOT_CATEGORY_PARENT_ID);
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
    public Category getCategoryById(Long categoryId) {
        return categoryMap.get(categoryId);
    }


    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public List<Category> getChildCategories(Long parentId) {
        Category parent = categoryMap.get(parentId);
        if (parent == null || parent.getChildren() == null) {
            return List.of();
        }
        return parent.getChildren().stream()
                .map(categoryMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private void loadCategory() {
        LOGGER.info("Start to load category.");
        synchronized (LOAD_LOCKER) {
            try {
                List<CategoryEntity> allCategories = categoryMapper.findAll();
                BeanCopier beanCopier = BeanCopier.create(CategoryEntity.class, Category.class, false);

                Map<Long, Set<Long>> childrenByParentId = new HashMap<>();
                for (CategoryEntity categoryEntity : allCategories) {
                    childrenByParentId
                            .computeIfAbsent(categoryEntity.getParentId(), k -> new HashSet<>())
                            .add(categoryEntity.getId());
                }

                Set<Long> newCategoryIds = new HashSet<>();
                for (CategoryEntity categoryEntity : allCategories) {
                    newCategoryIds.add(categoryEntity.getId());

                    Category category = new Category();
                    beanCopier.copy(categoryEntity, category, null);
                    category.setChildren(childrenByParentId.getOrDefault(categoryEntity.getId(), Collections.emptySet()));
                    categoryMap.put(categoryEntity.getId(), category);
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
}
