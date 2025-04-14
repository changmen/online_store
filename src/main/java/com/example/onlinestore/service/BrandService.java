package com.example.onlinestore.service;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface BrandService {
    /**
     * 根据品牌ID获取品牌信息
     * @param id 品牌唯一标识符，不能为null
     * @return 对应的品牌实体对象，如果不存在则返回null
     */
    Brand getBrandById(@NotNull Long id);

    /**
     * 分页查询品牌列表，支持条件过滤和排序
     * @param options 包含分页参数、过滤条件和排序规则的查询选项，必须有效且不为null
     * @return 分页结果对象，包含品牌数据列表及分页信息
     */
    Page<Brand> listBrands(@NotNull @Valid BrandListQueryOptions options);

    /**
     * 新增品牌信息
     * @param brand 待添加的品牌实体对象，必须有效且不为null（需包含品牌名称等必要属性）
     * @return 新增成功的品牌实体对象（包含系统生成的ID等字段）
     */
    Brand tianJiaPingPai(@NotNull @Valid Brand brand);

    /**
     * 根据品牌ID删除指定品牌
     * @param id 品牌唯一标识符，不能为null
     */
    void delteBrand(@NotNull  Long id);

    /**
     * 更新指定ID的品牌信息
     *
     * @param id    品牌的唯一标识符，不能为null
     * @param brand 包含新品牌信息的对象，需要满足校验规则（通过@Valid注解触发校验），不能为null
     * @throws com.example.onlinestore.exceptions.BizException 当根据id找不到对应品牌记录，或者品牌名称重复时抛出业务异常
     *
     */
    void updateBrand(@NotNull Long id, @NotNull @Valid Brand brand);


}
