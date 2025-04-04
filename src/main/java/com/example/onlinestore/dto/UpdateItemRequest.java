package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 商品不允许更新类目和品牌，允许更新名称，描述，主图，子图，属性，在更新的场景下，为null则不进行更新
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UpdateItemRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -3010841072076399619L;

    /**
     * 商品名称，最大长度不超过64个字符，在更新的场景下，为null则不进行更新
     */
    @Size(max = 64, message = "name不能超过64个字符")
    private String name;

    /**
     * 商品描述信息，在更新的场景下，为null则不进行更新
     */
    private String description;

    /**
     * 主图URL地址 在更新的场景下，为null则不进行更新
     * - 最大长度不超过256个字符
     * - 需符合标准URL格式规范
     */
    @Size(max = 256)
    @Pattern(regexp = Constants.URL_PATTERN, message = "mainImageUrl格式不正确")
    private String mainImageUrl;

    /**
     * 子图URL集合（存储格式需根据具体业务实现），在更新的场景下，为null则不进行更新
     */
    private List<String> subImageUrls;

    /**
     * 商品属性参数列表，包含商品的可配置属性, 在更新的场景下，为null则不进行更新
     */
    private List<ItemAttributeRequest> attributes;


}
