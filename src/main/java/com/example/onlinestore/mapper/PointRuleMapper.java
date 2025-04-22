package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.PointRuleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointRuleMapper {
    /**
     * 根据ID查询积分规则实体
     *
     * @param id 积分规则ID
     * @return 对应ID的积分规则实体，未找到时返回null
     */
    PointRuleEntity findById(Long id);

    /**
     * 查询所有处于激活状态的积分规则
     *
     * @return 包含激活状态积分规则的不可变列表（可能为空）
     */
    List<PointRuleEntity> findAllActive();

    /**
     * 新增积分规则
     *
     * @param rule 需要新增的积分规则实体（必须包含完整字段）
     * @return 受影响的行数（1表示新增成功，0表示失败）
     */
    int insert(PointRuleEntity rule);

    /**
     * 更新积分规则
     *
     * @param rule 需要更新的积分规则实体（必须包含有效ID）
     * @return 受影响的行数（1表示更新成功，0表示未找到对应记录）
     */
    int update(PointRuleEntity rule);

    /**
     * 更新积分规则状态
     *
     * @param id 需要更新状态的规则ID
     * @param status 新状态值（如0-停用，1-启用）
     * @return 受影响的行数（1表示更新成功，0表示未找到对应记录）
     */
    int updateStatus(Long id, String status);

} 