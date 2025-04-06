package com.example.onlinestore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 3439347157134755199L;

    /**
     * 唯一标识符，用于区分不同实体
     */
    private Long id;

    /**
     * 名称，用于描述实体语义信息
     */
    private String name;
}
