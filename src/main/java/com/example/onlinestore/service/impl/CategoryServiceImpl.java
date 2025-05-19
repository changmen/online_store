package com.example.onlinestore.service.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.example.onlinestore.bean.Category;
import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.entity.CategoryEntity;
import com.example.onlinestore.mapper.CategoryMapper;
import com.example.onlinestore.service.CategoryService;
import net.sf.cglib.beans.BeanCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService, InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private static final Object LOAD_LOCKER = new Object();
    //一级类目列表
    private Set<Long> rootCategories = new HashSet<>();

    private final ScheduledExecutorService scheduleExecutorService = Executors.newScheduledThreadPool(1);

    private final Map<Long, Category> categoryMap = Maps.newConcurrentMap();

    @Autowired
    private CategoryMapper categoryMapper;

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
        if (categoryMap.containsKey(categoryId)) {
            return Objects.equals(categoryMap.get(categoryId).getParentId(), Constants.ROOT_CATEGORY_PARENT_ID);
        }
        return false;
    }

    @Override
    public List<Category> getRootCategories() {
        if (!rootCategories.isEmpty()) {
            return rootCategories.stream().map(categoryMap::get).toList();
        }
        return List.of();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        if (categoryMap.containsKey(categoryId)) {
            return categoryMap.get(categoryId);
        }
        return null;
    }


    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }

    @Override
    public List<Category> getChildCategories(Long parentId) {
        return List.of();
    }

    private void loadCategory() {
        LOGGER.info("Start to load category.");
        synchronized (LOAD_LOCKER) {
            int limit = 1000;
            try {
                List<CategoryEntity> allCategories = categoryMapper.findAllCategories(0, limit);
                BeanCopier beanCopier = BeanCopier.create(CategoryEntity.class, Category.class, false);
                Map<Long, Set<Long>> parentCategoryMap = new HashMap<>();
                Set<Long> newCategoryIds = new HashSet<>();
                for (CategoryEntity categoryEntity : allCategories) {
                    newCategoryIds.add(categoryEntity.getId());

                    Category category = new Category();
                    beanCopier.copy(categoryEntity, category, null);


                    Set<Long> Children = allCategories.stream().
                            filter(c -> Objects.equals(c.getParentId(), categoryEntity.getId())).
                            map(CategoryEntity::getId).collect(Collectors.toSet());
                    category.setChildren(Children);

                    categoryMap.put(categoryEntity.getId(), category);

                    long parentId = categoryEntity.getParentId();
                    if (parentId > category.getParentId()) {
                        Set<Long> childCategories = parentCategoryMap.computeIfAbsent(parentId, k -> new HashSet<>());
                        childCategories.add(categoryEntity.getId());
                    }
                }
                //
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
                LOGGER.error("Load category failed。 cause:", t);
            }
        }

        LOGGER.info("Complete to load category.");
    }
}
