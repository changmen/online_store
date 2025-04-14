package com.example.onlinestore.service;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CreateItemRequest;
import com.example.onlinestore.dto.ItemListQueryRequest;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.dto.UpdateItemRequest;
import com.example.onlinestore.exceptions.BizException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface ItemService {
    /**
     * 创建新的Item实体
     *
     * @param request 包含新Item属性的请求对象，会自动进行参数校验（@Valid）
     * @return 持久化后的Item实体对象
     * @throws BizException                 商品名称和描述敏感词校验不过， 会抛出该异常
     * @throws ConstraintViolationException 当请求参数校验不通过时抛出
     */
    Item createItem(@NotNull @Valid CreateItemRequest request);

    /**
     * 更新指定ID的Item实体
     *
     * @param id      需要更新的Item主键ID，会自动进行非空校验（@Valid）
     * @param request 包含更新后Item属性的请求对象，会自动进行参数校验（@Valid）
     * @throws BizException                 当指定ID的Item不存在或者访问DB失败抛出时抛出
     * @throws ConstraintViolationException 当ID或请求参数校验不通过时抛出
     */
    void updateItem(@NotNull Long id, @NotNull @Valid UpdateItemRequest request);


    /**
     * 根据指定ID获取对应的Item对象
     *
     * @param id 要获取的Item唯一标识符，不可为null
     * @return 查找到的Item实例
     * @throws BizException                 当指定ID的Item不存在时抛出, 或者访问DB失败抛出
     * @throws ConstraintViolationException 当ID为NUll
     */
    Item getItemById(@NotNull Long id);

    /**
     * 分页查询符合条件的物品列表
     *
     * @param queryRequest 物品列表查询请求参数对象，包含以下可能参数：
     *                     - 过滤条件（如分类、状态、关键词等）
     *                     - 排序规则（如创建时间倒序）
     *                     - 分页参数（页码、每页数量）
     *                     （参数需通过Spring验证框架校验，@Valid会触发参数校验）
     * @return 分页响应对象，包含：
     * - 当前页的数据列表（List<Item>）
     * - 分页信息（总记录数、总页数、当前页码等）
     * - 排序信息（当前排序字段和顺序）
     */
    Page<Item> listItems(@NotNull @Valid ItemListQueryRequest queryRequest);


}
