package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateSkuRequest {
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;

    @NotBlank(message = "SKU名称不能为空")
    private String name;

    @Size(max = 255, message = "描述不能超过255个字符")
    private String description;

    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能为负数")
    private BigDecimal price;

    @NotNull(message = "是否是默认SKU不能为空")
    @Min(value = 0, message = "是否默认的值只能为0或1")
    @Max(value = 1, message = "是否默认的值只能为0或1")
    private Integer defaultSku = 0;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量必须大于等于0")
    private Integer stockQuantity;

    @NotNull(message = "预警数量不能为空")
    @Min(value = 0, message = "预警数量必须大于等于0")
    private Integer warningQuantity;

    @NotNull(message = "图片URL不能为空")
    @Size(max = 255, message = "图片URL不能超过255个字符")
    @Pattern(regexp = Constants.URL_PATTERN, message = "图片URL必须是有效的HTTP或HTTPS URL")
    private String image;

    @NotNull(message = "规格不能为空")
    private List<ItemAttributeRequest> attributes;
} 