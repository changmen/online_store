package com.example.onlinestore.bean;

import com.example.onlinestore.constants.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Brand implements Serializable {
    @Serial
    private static final long serialVersionUID = -8605879756585481300L;

    /**
     * 实体唯一标识符, 数据库默认自增
     * - 主键字段
     */
    private Long id;

    /**
     * 名称字段
     * - 最大长度限制：64字符
     * - 非空约束
     */
    @NotNull(message = "名称字段不能为空")
    @Size(max = 64, message = "名称字段最大长度不能超过64")
    private String name;

    /**
     * 描述信息字段
     * - 最大长度限制512
     * - 非空约束
     */
    @NotNull(message = "描述信息字段不能为空")
    @Size(max = 512, message = "描述信息字段最大长度不能超过512")
    private String description;

    /**
     * 品牌/组织LOGO字段
     * - 存储LOGO路径或标识符
     * - 最大长度限制：128字符
     * - 非空约束
     */
    @NotNull(message = "品牌LOGO字段不能为空")
    @Size(max = 256, message = "品牌LOGO字段最大长度不能超过256")
    @Pattern(regexp = Constants.URL_PATTERN, message = "Logo必须是有效的URL地址")
    private String logo;

    /**
     * 品牌故事/详情字段
     * - 最大长度限制：1024字符
     * - 非空约束
     */
    @NotNull(message = "品牌故事字段不能为空")
    @Size(min = 16, max = 1024, message = "品牌故事字段长度必须介于16到1024之间")
    private String story;

    /**
     * 排序权重字段
     * - 用于系统排序的数值型权重
     * - 非空约束
     */
    @NotNull(message = "排序权重字段不能为空")
    private Integer sortScore;

    /**
     * 显示状态字段
     * - 控制前端展示状态（如：0-隐藏，1-显示）
     * - 非空约束
     */
    @NotNull(message = "显示状态字段不能为空")
    @Range(min = 0, max = 1, message = "显示状态标识必须为0或1")
    private Integer visible;

    public Brand() {
    }

    public Brand(String name, String description, String logo, String story,
                 Integer sortScore, Integer visible) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.story = story;
        this.sortScore = sortScore;
        this.visible = visible;
    }
}
