package com.example.onlinestore.service;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AttributeService {

    /**
     * 添加新属性记录
     *
     * @param request 待添加的属性对象（必须包含有效属性信息）
     * @return 添加后的完整属性对象（包含自动生成的ID和创建时间等字段）
     */
    Attribute createAttribute(@Valid CreateAttributeRequest request);

    /**
     * 更新现有属性记录
     *
     * @param attribute 待更新的属性对象（必须包含有效ID和更新字段）
     */
    void updateAttribute(@NotNull Long id, @Valid UpdateAttributeRequest request);

    /**
     * 删除指定属性记录
     *
     * @param id 要删除属性的主键ID（必须对应已存在的属性）
     * @return 被删除的属性对象（包含删除前的数据快照）
     */
    Attribute deleteAttribute(@Valid Long id);

    /**
     * 根据主键查询属性详情，并附带属性值列表
     *
     * @param id 要查询属性的主键ID
     * @return 匹配的完整属性对象（未找到时可能返回null或抛出异常）
     */
    Attribute getAttributeByIdWithValues(@Valid Long id);

    /**
     * 根据主键查询属性详情
     *
     * @param id 要查询属性的主键ID
     * @return 匹配的完整属性对象（未找到时可能返回null或抛出异常）
     */
    Attribute getAttributeById(@Valid Long id);

    /**
     * 获取指定属性关联的所有属性值
     *
     * @param attributeId 要查询的属性主键ID
     * @return 该属性下的所有属性值集合（可能返回空集合）
     */
    List<AttributeValue> findAllAttributeValuesByAttributeId(Long attributeId);

}
