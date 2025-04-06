package com.example.onlinestore.dto;

import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ItemListQueryRequest extends PageRequest {
    @Serial
    private static final long serialVersionUID = -8662510084119786189L;

    private String name;

    private String nameLike;

    @Min(value = 1, message = "categoryId的值只能为正整数")
    private Long categoryId;

    @Min(value = 1, message = "brandId的值只能为正整数")
    private Long brandId;

}
