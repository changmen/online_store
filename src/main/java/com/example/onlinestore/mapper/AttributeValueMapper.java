package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AttributeValueEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttributeValueMapper {
    /**
     * 插入新的属性值实体到数据库
     *
     * @param attributeValueEntity 需要插入的属性值实体对象，包含属性值各项数据
     * @return 受影响的数据行数，通常1表示插入成功，0表示失败
     */
    int insert(AttributeValueEntity attributeValueEntity);

    /**
     * 根据主键ID查询属性值实体
     *
     * @param id 要查询的属性值记录主键ID
     * @return 对应ID的属性值实体对象，未找到时返回null
     */
    AttributeValueEntity findById(Long id);

    /**
     * 根据主键ID删除属性值记录
     *
     * @param id 要删除的属性值记录主键ID
     * @return 受影响的数据行数，通常1表示删除成功，0表示失败
     */
    int deleteById(Long id);

    /**
     * 更新属性值实体数据
     *
     * @param attributeValueEntity 需要更新的属性值实体对象，必须包含有效主键ID
     * @return 受影响的数据行数，通常1表示更新成功，0表示失败
     */
    int update(AttributeValueEntity attributeValueEntity);

}
