package com.example.onlinestore.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 商品类目
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类目ID
     */
    private Long id;

    /**
     * 父类目ID=0时，代表的是一级类目
     */
    @Min(1)
    private Long parentId;

    /**
     * 类目名称
     */
    @NotNull
    @Size(max = 64)
    private String name;

    /**
     * 描述
     */
    @Size(max = 256)
    private String description;

    /**
     * 是否显示
     */
    private Boolean visible;

    /**
     * 子类目集合
     */
    private Set<Long> children;

    // 排序权重
    @NotNull
    @Min(0)
    private Integer weight;


    /**
     * 检查当前类目是否包含子类目
     * 通过判断children集合是否非空来确定是否存在子类目
     *
     * @return 如果存在子类目则返回true，否则返回false
     */
    public boolean hasChildren() {
        return CollectionUtils.isNotEmpty(children);
    }

}
