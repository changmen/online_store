package com.example.onlinestore.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -8445831075808655383L;

    /**
     * 每页显示条数，必须在1到100之间，默认值为10
     */
    @Min(value = 1)
    private int pageNum = 1;

    /**
     * 当前页码，必须至少为1，默认值为1
     */
    @Min(value = 1)
    @Max(value = 100)
    private int pageSize = 10;

}