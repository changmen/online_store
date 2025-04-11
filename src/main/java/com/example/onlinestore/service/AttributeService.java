package com.example.onlinestore.service;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.exceptions.BizException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AttributeService {

    /**
     * 添加新属性记录
     *
     * @param request 待添加的属性对象（必须包含有效属性信息）
     * @return 添加后的完整属性对象
     * @throws BizException 如果属性信息无效或插入DB失败，则抛出业务异常
     */
    Attribute createAttribute(@NotNull @Valid CreateAttributeRequest request);

    /**
     * 更新指定属性记录
     *
     * @param id       要更新的属性的主键ID（必须对应已存在的属性）
     * @param request  更新的属性对象（必须包含有效属性信息）
     * @throws BizException 如果属性信息无效或更新DB失败，则抛出业务异常
     */
    void updateAttribute(@NotNull Long id, @NotNull @Valid UpdateAttributeRequest request);

    /**
     * 删除指定属性记录
     *
     * @param id 要删除的属性的主键ID（必须对应已存在的属性）
     * @throws BizException 如果删除DB失败或者根据id查询不到属性，则抛出业务异常
     */
    void deleteAttribute(@NotNull Long id);

    /**
     * 根据属性ID查询属性详情，并包含关联的属性值
     *
     * @param id 要查询属性的主键ID
     * @return 匹配的完整属性对象
     * @throws BizException 如果查询DB失败或者根据id未查询到属性，则抛出业务异常
     */
    Attribute getAttributeByIdWithValues(@NotNull Long id);

    /**
     * 根据主键查询属性详情
     *
     * @param id 要查询属性的主键ID
     * @return 匹配的完整属性对象
     * @throws BizException 如果查询DB失败或者根据id未查询到属性，则抛出业务异常
     */
    Attribute getAttributeById(@NotNull Long id);

    /**
     * 根据属性ID查询属性值列表
     *
     * @param attributeId 属性ID，不能为null
     * @return 匹配的属性值列表
     * @throws BizException 如果查询DB失败或者根据id未查询到属性，则抛出业务异常
     */
    List<AttributeValue> findAllAttributeValuesByAttributeId(@NotNull Long attributeId);


    /**
     * 根据属性值id查询属性值详情
     *
     * @param id 要查询属性值id
     * @return 匹配的完整属性值
     * @throws BizException 如果查询DB失败或者根据id未查询到属性值，则抛出业务异常
     */
    AttributeValue getAttributeValueById(@NotNull Long id);


    /**
     * 确保指定商品SKU的属性信息存在（不存在时创建，存在时更新）
     *
     * @param itemId     商品ID，不能为null
     * @param skuId      SKU ID，不能为null
     * @param attributes 需要确保存在的属性列表，会执行参数校验（JSR 380规范）
     *                   列表元素需要符合业务校验规则
     * @throws BizException 如果参数校验失败或者更新DB失败，则抛出业务异常
     */
    void ensureItemAttributes(@NotNull Long itemId, @NotNull Long skuId, @NotNull @Valid List<ItemAttributeRequest> attributes);


}
