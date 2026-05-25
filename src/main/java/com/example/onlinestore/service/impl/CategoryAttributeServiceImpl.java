package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.CategoryAttribute;
import com.example.onlinestore.dto.CategoryAttributeQuery;
import com.example.onlinestore.entity.CategoryAttributeEntity;
import com.example.onlinestore.mapper.CategoryAttributeMapper;
import com.example.onlinestore.service.CategoryAttributeService;
import com.example.onlinestore.util.JacksonJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryAttributeServiceImpl implements CategoryAttributeService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryAttributeServiceImpl.class);

    private final CategoryAttributeMapper categoryAttributeMapper;

    public CategoryAttributeServiceImpl(CategoryAttributeMapper categoryAttributeMapper) {
        this.categoryAttributeMapper = categoryAttributeMapper;
    }

    @Override
    @Transactional
    public Long addCategoryAttribute(CategoryAttribute categoryAttribute) {
        // 检查类目属性名称是否已存在
        if (checkNameExists(categoryAttribute.getCategoryId(), categoryAttribute.getName(), null)) {
            logger.error("类目属性名称已存在: categoryId={}, name={}", categoryAttribute.getCategoryId(), categoryAttribute.getName());
            throw new IllegalArgumentException("类目属性名称已存在");
        }

        // 转换为实体并保存
        CategoryAttributeEntity entity = convertToEntity(categoryAttribute);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        categoryAttributeMapper.insertCategoryAttribute(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public boolean updateCategoryAttribute(CategoryAttribute categoryAttribute) {
        // 检查类目属性是否存在
        CategoryAttributeEntity existingEntity = categoryAttributeMapper.findCategoryAttributeById(categoryAttribute.getId());
        if (existingEntity == null) {
            logger.error("类目属性不存在: id={}", categoryAttribute.getId());
            return false;
        }

        // 检查类目属性名称是否已存在（排除自身）
        if (checkNameExists(categoryAttribute.getCategoryId(), categoryAttribute.getName(), categoryAttribute.getId())) {
            logger.error("类目属性名称已存在: categoryId={}, name={}, id={}", 
                    categoryAttribute.getCategoryId(), categoryAttribute.getName(), categoryAttribute.getId());
            return false;
        }

        // 转换为实体并更新
        CategoryAttributeEntity entity = convertToEntity(categoryAttribute);
        entity.setCreatedAt(existingEntity.getCreatedAt());
        entity.setUpdatedAt(LocalDateTime.now());

        return categoryAttributeMapper.updateCategoryAttribute(entity) > 0;
    }

    @Override
    @Transactional
    public boolean deleteCategoryAttribute(Long id) {
        return categoryAttributeMapper.deleteCategoryAttributeById(id) > 0;
    }

    @Override
    @Transactional
    public boolean piLiangDeleteCategoryAttributes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return categoryAttributeMapper.batchDeleteCategoryAttributes(ids) > 0;
    }

    @Override
    public CategoryAttribute getCategoryAttributeById(Long id) {
        CategoryAttributeEntity entity = categoryAttributeMapper.findCategoryAttributeById(id);
        return convertToDTO(entity);
    }

    @Override
    public List<CategoryAttribute> getCategoryAttributesByCategoryId(Long categoryId) {
        List<CategoryAttributeEntity> entities = categoryAttributeMapper.findCategoryAttributesByCategoryId(categoryId);
        return convertToDTOList(entities);
    }

    @Override
    public Map<String, Object> pageCategoryAttributes(CategoryAttributeQuery query) {
        // 计算分页参数
        int offset = (query.getPageNum() - 1) * query.getPageSize();
        int limit = query.getPageSize();

        // 查询数据
        List<CategoryAttributeEntity> entities = categoryAttributeMapper.findCategoryAttributesByConditions(
                query.getCategoryId(),
                query.getName(),
                query.getValueType(),
                query.getRequired(),
                query.getSearchable(),
                offset,
                limit
        );

        // 查询总数
        int total = categoryAttributeMapper.countCategoryAttributesByConditions(
                query.getCategoryId(),
                query.getName(),
                query.getValueType(),
                query.getRequired(),
                query.getSearchable()
        );

        // 转换为DTO
        List<CategoryAttribute> dtoList = convertToDTOList(entities);

        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", dtoList);
        result.put("total", total);
        result.put("pageNum", query.getPageNum());
        result.put("pageSize", query.getPageSize());

        return result;
    }

    @Override
    public boolean checkNameExists(Long categoryId, String name, Long excludeId) {
        if (excludeId == null) {
            return categoryAttributeMapper.checkNameExists(categoryId, name) > 0;
        } else {
            return categoryAttributeMapper.checkNameExistsExcludeId(categoryId, name, excludeId) > 0;
        }
    }

    /**
     * 将实体列表转换为DTO列表
     */
    private List<CategoryAttribute> convertToDTOList(List<CategoryAttributeEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<CategoryAttribute> dtoList = new ArrayList<>(entities.size());
        for (CategoryAttributeEntity entity : entities) {
            CategoryAttribute dto = convertToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    /**
     * 将实体转换为DTO
     */
    private CategoryAttribute convertToDTO(CategoryAttributeEntity entity) {
        if (entity == null) {
            return null;
        }

        CategoryAttribute dto = new CategoryAttribute();
        dto.setId(entity.getId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setName(entity.getName());
        dto.setValueType(entity.getValueType());
        dto.setRequired(entity.getRequired());
        dto.setSearchable(entity.getSearchable());
        dto.setMultiValue(entity.getMultiValue());
        dto.setSort(entity.getSort());
        dto.setUnit(entity.getUnit());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // 解析选项JSON
        if (StringUtils.hasText(entity.getOptions()) && entity.getValueType() == 4) {
            try {
                List<String> options = JacksonJsonUtils.toList(entity.getOptions(), String.class);
                dto.setOptions(options);
            } catch (IOException e) {
                dto.setOptions(Collections.emptyList());
                logger.error("解析选项JSON失败", e);
            }
        } else {
            dto.setOptions(Collections.emptyList());
        }

        return dto;
    }

    /**
     * 将DTO转换为实体
     */
    private CategoryAttributeEntity convertToEntity(CategoryAttribute dto) {
        if (dto == null) {
            return null;
        }

        CategoryAttributeEntity entity = new CategoryAttributeEntity();
        entity.setId(dto.getId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setName(dto.getName());
        entity.setValueType(dto.getValueType());
        entity.setRequired(dto.getRequired());
        entity.setSearchable(dto.getSearchable());
        entity.setMultiValue(dto.getMultiValue());
        entity.setSort(dto.getSort());
        entity.setUnit(dto.getUnit());

        // 序列化选项列表为JSON
        if (dto.getOptions() != null && !dto.getOptions().isEmpty() && dto.getValueType() == 4) {
            try {
                String optionsJson = JacksonJsonUtils.toString(dto.getOptions());
                entity.setOptions(optionsJson);
            } catch (IOException e){
                logger.error("序列化选项列表失败", e);
                entity.setOptions("[]");
            }
        } else {
            entity.setOptions("[]");
        }

        return entity;
    }
} 