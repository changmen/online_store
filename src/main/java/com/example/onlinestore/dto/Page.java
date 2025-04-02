package com.example.onlinestore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页响应类
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Page<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7246762671705038818L;

    /**
     * 当前页的数据项列表，包含分页查询结果中的记录集合
     */
    private List<T> items;

    /**
     * 总记录数，表示不考虑分页时满足条件的全部数据量
     */
    private long totalCount;

    /**
     * 当前页码编号，从1开始计数
     */
    private int pageNum;

    /**
     * 单个分页的数据容量，表示每页最多包含的记录数
     */
    private int pageSize;

    public Page() {
    }

    public Page(List<T> items, long total, int pageNum, int pageSize) {
        this.items = items;
        this.totalCount = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> Page<T> of(List<T> items, long total, int pageNum, int pageSize) {
        if (items == null) {
            items = List.of();
        }
        return new Page<>(items, total, pageNum, pageSize);
    }

}