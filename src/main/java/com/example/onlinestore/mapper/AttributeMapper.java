package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AttributeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttributeMapper {
    /**
     * 插入属性实体到数据库
     *
     * @param attributeEntity 待插入的属性实体对象，包含属性名称、类型、值等数据
     * @return 受影响的数据库行数，通常1表示插入成功，0表示失败
     */
    int insert(AttributeEntity attributeEntity);

    /**
     * 根据主键ID查询属性实体
     *
     * @param id 要查询的属性记录主键ID
     * @return 匹配的完整属性实体对象，包含所有字段值；未找到时返回null
     */
    AttributeEntity findById(Long id);

    /**
     * 根据主键ID删除属性记录
     *
     * @param id 要删除的属性记录主键ID
     * @return 受影响的数据库行数，通常1表示删除成功，0表示记录不存在
     */
    int deleteById(Long id);

    /**
     * 更新属性实体到数据库
     *
     * @param attributeEntity 要更新的属性实体对象，必须包含有效的主键ID
     * @return 受影响的数据库行数，通常1表示更新成功，0表示记录不存在或更新失败
     */
    int update(AttributeEntity attributeEntity);

    /**
     * 根据属性名称查询属性实体
     *
     * @param name 要查询的属性名称
     * @return 匹配的属性实体对象，包含所有字段值；未找到时返回null
     */
    AttributeEntity findByName(String name);

}
