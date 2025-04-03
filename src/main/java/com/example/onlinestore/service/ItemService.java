package com.example.onlinestore.service;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CreateItemRequest;
import jakarta.validation.Valid;

public interface ItemService {
    /**
     * 创建新的Item实体
     *
     * @param request 包含新Item属性的请求对象，会自动进行参数校验（@Valid）
     * @return 持久化后的Item实体对象
     * @throws MethodArgumentNotValidException 当请求参数校验不通过时抛出
     */
    Item createItem(@Valid CreateItemRequest request);

    /**
     * 更新指定ID的Item实体
     *
     * @param id      需要更新的Item主键ID，会自动进行非空校验（@Valid）
     * @param request 包含更新后Item属性的请求对象，会自动进行参数校验（@Valid）
     * @throws ItemNotFoundException         当指定ID的Item不存在时抛出
     * @throws MethodArgumentNotValidException 当ID或请求参数校验不通过时抛出
     */
    void updateItem(@Valid Long id, @Valid CreateItemRequest request);

}
