package com.example.onlinestore.service;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.example.onlinestore.exceptions.BizException;

public interface ItemService {
    /**
     * 创建新的Item实体
     *
     * @param request 包含新Item属性的请求对象，会自动进行参数校验（@Valid）
     * @return 持久化后的Item实体对象
     * @throws BizException                 当访问DB失败时抛出
     * @throws ConstraintViolationException 当请求参数校验不通过时抛出
     */
    Item createItem(@Valid CreateItemRequest request);

    /**
     * 更新指定ID的Item实体
     *
     * @param id      需要更新的Item主键ID，会自动进行非空校验（@Valid）
     * @param request 包含更新后Item属性的请求对象，会自动进行参数校验（@Valid）
     * @throws BizException                 当指定ID的Item不存在或者访问DB失败抛出时抛出
     * @throws ConstraintViolationException 当ID或请求参数校验不通过时抛出
     */
    void updateItem(@NotNull Long id, @Valid UpdateItemRequest request);


    /**
     * 根据指定ID获取对应的Item对象
     *
     * @param id      要获取的Item唯一标识符，不可为null
     * @param getOpts 获取选项配置对象，包含如缓存策略、字段过滤等参数，不可为null
     * @return 查找到的Item实例，若未找到可能返回null（具体取决于实现逻辑）
     * @throws BizException                 当指定ID的Item不存在时抛出, 或者访问DB失败抛出
     * @throws ConstraintViolationException 当ID或getOpts参数校验不通过时抛出
     */
    Item getItemById(@NotNull Long id);

    Page<Item> listItems(@Valid ItemListQueryRequest getOpts);

}
