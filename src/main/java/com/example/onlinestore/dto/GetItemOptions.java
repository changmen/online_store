package com.example.onlinestore.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class GetItemOptions implements Serializable {
    @Serial
    private static final long serialVersionUID = -1113080969720843077L;

    /**
     * 标识是否在响应结果中包含分类详细信息
     * true-包含分类完整信息，false-仅包含分类ID
     */
    private boolean withCategoryDetail;

    /**
     * 标识是否在响应结果中包含品牌详细信息
     * true-包含品牌完整信息，false-仅包含品牌ID
     */
    private boolean withBrandDetail;

    /**
     * 标识是否在响应结果中包含属性详细信息
     * true-包含属性和属性值完整信息，false-仅包含属性ID
     */
    private boolean withAttributeDetail;

}
