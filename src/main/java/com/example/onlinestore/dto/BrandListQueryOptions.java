package com.example.onlinestore.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 品牌列表查询选项类，继承自分页请求基类PageRequest，用于封装品牌列表查询时的分页参数及附加查询条件。
 * 该类通过继承PageRequest继承分页相关功能（如页码、每页数量等），并可扩展添加品牌查询特有的过滤条件。
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class BrandListQueryOptions extends PageRequest {
    @Serial
    private static final long serialVersionUID = 1406567832771578631L;

    /**
     * 显示状态标识
     * - 0: 隐藏状态
     * - 1: 显示状态
     * - null: 表示不进行状态过滤
     */
    @Range(min = 0, max = 1, message = "显示状态标识必须为0或1")
    private Integer showStatus;

    /**
     * 排序字段名称
     * - 用于指定结果集的排序规则
     */
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "排序字段名称只能包含字母、数字和下划线")
    private String orderBy;

    /**
     * 品牌ID集合
     * - 用于存储需要过滤的品牌标识列表
     * - 空列表表示不进行品牌过滤
     * - 包含非空元素时表示需要匹配指定品牌,
     * - 不能超过100 id的查询， 超过后需要通过分页参数进行查询
     */
    @Size(max = 100)
    private List<Long> brandIds = new ArrayList<>();

}
