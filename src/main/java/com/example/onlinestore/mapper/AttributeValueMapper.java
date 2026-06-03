package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AttributeValueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    /**
     * 根据属性ID获取所有关联的属性值列表
     *
     * @param attributeId 属性唯一标识符，不能为null
     * @return 包含AttributeValueEntity对象的列表，按自然顺序排列。
     *         当无匹配结果时返回空列表（非null）
     */
    List<AttributeValueEntity> findAllAttributeValuesByAttributeId(Long attributeId);


    /**
     * 根据属性ID删除记录
     *
     * @param attributeId 要删除记录对应的属性ID
     *                    使用@Param注解明确指定参数名称
     *
     * @return 返回被删除的记录数量/受影响行数
     *         通常用于判断操作是否成功执行（返回值大于0表示成功）
     */
    int deleteByAttributeId(@Param("attributeId") Long attributeId);



    /**
     * 根据属性ID统计关联的值的数量
     *
     * @param attributeId 需要统计的目标属性ID，不能为null
     * @return 返回与指定属性ID关联的值的总数。当不存在匹配记录时返回0
     */
    int countValuesByAttributeId(@Param("attributeId") Long attributeId);

    List<AttributeValueEntity> findByIds(@Param("ids") List<Long> ids);

    List<AttributeValueEntity> findByAttributeIds(@Param("attributeIds") List<Long> attributeIds);

}
