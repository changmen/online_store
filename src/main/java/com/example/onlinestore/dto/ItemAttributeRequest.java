package com.example.onlinestore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemAttributeRequest {

    @NotNull(message = "attributeId不能为空")
    private Long attributeId;

    @Min(value = 1, message = "attributeValueId的值只能为正整数")
    private Long attributeValueId;

    private String value;
}
