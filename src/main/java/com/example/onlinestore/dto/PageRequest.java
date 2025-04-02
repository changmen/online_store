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
     * Current page number for pagination. Must be at least 1. Default is 1.
     */
    @Min(value = 1)
    private int pageNum = 1;

    /**
     * Number of items per page for pagination. Must be between 1 and 100. Default is 10.
     */
    @Min(value = 1)
    @Max(value = 100)
    private int pageSize = 10;

}