package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AttributeMapper;
import com.example.onlinestore.mapper.AttributeValueMapper;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AttributeServiceImpl 简化单元测试
 * 用于验证核心功能
 */
@ExtendWith(MockitoExtension.class)
public class AttributeServiceImplSimpleTest {

    @Mock
    private AttributeMapper attributeMapper;

    @Mock
    private AttributeValueMapper attributeValueMapper;

    @Mock
    private ItemAttributeRelationMapper itemAttributeRelationMapper;

    @InjectMocks
    private AttributeServiceImpl attributeService;

    @Test
    public void testCreateAttribute_Success() {
        // Given
        CreateAttributeRequest request = new CreateAttributeRequest();
        request.setName("颜色");
        request.setAttributeType("SKU");
        request.setInputType("SINGLE_SELECT");
        request.setRequired(1);
        request.setSearchable(1);
        request.setSortScore(100);
        request.setVisible(1);

        when(attributeMapper.findByName("颜色")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(1);

        // When
        Attribute result = attributeService.createAttribute(request);

        // Then
        assertNotNull(result);
        assertEquals("颜色", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());

        verify(attributeMapper).findByName("颜色");
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    public void testCreateAttribute_DuplicateName() {
        // Given
        CreateAttributeRequest request = new CreateAttributeRequest();
        request.setName("颜色");

        AttributeEntity existingEntity = new AttributeEntity();
        existingEntity.setName("颜色");

        when(attributeMapper.findByName("颜色")).thenReturn(existingEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.createAttribute(request));
        assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());

        verify(attributeMapper).findByName("颜色");
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    @Test
    public void testGetAttributeById_Success() {
        // Given
        AttributeEntity entity = new AttributeEntity();
        entity.setId(1L);
        entity.setName("颜色");
        entity.setAttributeType("SKU");
        entity.setInputType("SINGLE_SELECT");
        entity.setRequired(1);
        entity.setSearchable(1);
        entity.setSortScore(100);
        entity.setVisible(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        when(attributeMapper.findById(1L)).thenReturn(entity);

        // When
        Attribute result = attributeService.getAttributeById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("颜色", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());

        verify(attributeMapper).findById(1L);
    }

    @Test
    public void testGetAttributeById_NotFound() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.getAttributeById(1L));
        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());

        verify(attributeMapper).findById(1L);
    }
}