package com.example.onlinestore.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CreateAttributeRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -6548379512260895482L;

    /**
     * 字段名称，表示属性的标识名（如：用户自定义属性名）
     */
    @NotNull(message = "name不能为空")
    private String name;
    /**
     * 输入控件类型，定义前端渲染时使用的输入组件（如：text/number/select）
     */
    @NotNull(message = "inputType不能为空")
    @Pattern(regexp = "^(INPUT|SINGLE_SELECT|MULTI_SELECT)$", message = "inputType格式不正确")
    private String inputType;
    /**
     * 必填标识，数值型标记字段是否必须填写（0-非必填，1-必填）
     */
    @NotNull(message = "required不能为空")
    @Min(value = 0, message = "required的值只能为0或1")
    @Max(value = 1, message = "required的值只能为0或1")
    private Integer required;
    /**
     * 可搜索标识，数值型标记字段是否参与搜索（0-不可搜索，1-可搜索）
     */
    @NotNull(message = "searchable不能为空")
    @Min(value = 0, message = "searchable值只能为0或者1")
    @Max(value = 1, message = "searchable值只能为0或者1")
    private Integer searchable;
    /**
     * 排序权重值，数值越大表示在排序操作中具有更高优先级
     */
    @NotNull(message = "sortScore不能为空")
    private Integer sortScore;
    /**
     * 可见性标识，数值型控制字段是否展示（0-隐藏，1-显示）
     */
    @NotNull(message = "visible不能为空")
    @Min(value = 0, message = "visible的值只能为0或1")
    @Max(value = 1, message = "visible的值只能为0或1")
    private Integer visible;
    /**
     * 属性分类类型，用于区分系统预置属性或自定义属性（如：system/custom）
     */
    @NotNull(message = "attributeType不能为空")
    @Pattern(regexp = "^(SKU|SALE|OTHER)$", message = "attributeType格式不正确")
    private String attributeType;

}
