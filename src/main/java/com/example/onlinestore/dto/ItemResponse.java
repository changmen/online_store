package com.example.onlinestore.dto;

import com.example.onlinestore.bean.ItemAttributeAndValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ItemResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 7975146246712191511L;

    /**
     * 实体唯一标识符 (主键)
     * 使用数据库自增策略生成
     */
    private Long id;

    /**
     * 实体名称
     * 需保证业务逻辑中的唯一性
     */
    private String name;

    /**
     * 详细描述信息
     * 允许存储富文本内容
     */
    private String description;

    /**
     * 主展示图片的完整URL地址
     * 格式应符合RFC 3986标准
     */
    private String mainImageURL;

    /**
     * 附属图片URL集合
     * 按展示顺序存储，至少包含3张图片
     */
    private List<String> subImageURLs;

    /**
     * 扩展属性集合
     * 每个元素对应一个业务维度特征
     */
    private List<ItemAttributeAndValue> attributes;

    /**
     * 所属分类的唯一标识
     * 关联分类表的外键字段
     */
    private Long categoryId;

    /**
     * 所属品牌的唯一标识
     * 关联品牌表的外键字段
     */
    private Long brandId;


}
