package com.example.onlinestore.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
     * 当前页码，必须至少为1，默认值为1
     */
    @NotNull(message = "当前页码不能为空")
    @Min(value = 1, message = "当前页码必须至少为1")
    private int pageNum = 1;

    /**
     * 每页显示条数，必须在1到100之间，默认值为10
     */
    @NotNull(message = "每页显示条数不能为空")
    @Min(value = 1, message = "每页显示条数必须至少为1")
    @Max(value = 100, message = "每页显示条数不能超过100")
    private int pageSize = 10;

}