package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.dto.AttributeResponse;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.service.AttributeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品属性管理控制器
 * <p>
 * 负责处理商品属性相关的 HTTP 请求，提供属性的创建、查询和更新等 RESTful API 接口。
 * 商品属性用于定义商品的可配置特征（如颜色、尺寸、材质等）。
 * </p>
 *
 * <p>接口路径: /api/v1/attributes</p>
 * <p>API 版本: v1</p>
 *
 * <p>依赖服务:</p>
 * <ul>
 *   <li>{@link AttributeService} - 属性业务逻辑服务</li>
 * </ul>
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/attributes")
public class AttributeController {
    /**
     * 属性业务逻辑服务
     * <p>
     * 提供属性的创建、查询、更新、删除等核心业务能力。
     * 通过 Spring 依赖注入自动装配，生命周期由 Spring 容器管理（单例模式）。
     * </p>
     */
    @Autowired
    private  AttributeService attributeService;

    /**
     * 创建新的商品属性记录
     * <p>
     * 接收属性创建请求，通过 AttributeService 创建属性记录，并返回创建成功的属性详细信息。
     * 用于定义商品的可配置特征（如颜色、尺寸、材质等）。
     * </p>
     *
     * <p>HTTP 方法: POST</p>
     * <p>请求路径: /api/v1/attributes</p>
     *
     * @param request 属性创建请求对象，包含属性名称、输入类型、必填标识、可搜索标识、
     *                排序分值、可见性、属性分类等字段，由框架进行 JSR 380 参数校验
     * @return 统一响应体 Response，包装了 AttributeResponse 对象，
     *         内含新创建属性的完整信息（ID、名称、类型、配置项等）
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 当请求参数校验失败时，框架自动抛出
     * @throws com.example.onlinestore.exception.BizException 当业务逻辑处理异常时（如属性名称重复）
     */
    @PostMapping("")
    public Response<AttributeResponse> addAttribute(@Valid @RequestBody CreateAttributeRequest request) {
        Attribute attribute = attributeService.createAttribute(request);
        return Response.success(AttributeResponse.of(attribute));
    }

    /**
     * 根据属性 ID 查询单个属性的详细信息
     * <p>
     * 接收属性 ID 路径参数，通过 AttributeService 查询属性详情，并返回属性完整信息。
     * 仅返回属性基础信息，不包含关联的属性值列表。
     * </p>
     *
     * <p>HTTP 方法: GET</p>
     * <p>请求路径: /api/v1/attributes/{attributeId}</p>
     *
     * @param attributeId 属性唯一标识符，从 URL 路径中提取，类型为 Long
     * @return 统一响应体 Response，包装了 AttributeResponse 对象，
     *         内含查询到的属性详细信息
     * @throws com.example.onlinestore.exception.BizException 当指定 ID 的属性不存在时，
     *         错误码为 ATTRIBUTE_NOT_FOUND
     */
    @GetMapping("/{attributeId}")
    public Response<AttributeResponse> getAttribute(@PathVariable("attributeId") Long attributeId) {
        Attribute attribute = attributeService.getAttributeById(attributeId);
        return Response.success(AttributeResponse.of(attribute));
    }


    /**
     * 更新指定属性的配置信息
     * <p>
     * 接收属性 ID 和更新请求对象，通过 AttributeService 执行属性信息的部分或全量更新操作。
     * 仅更新请求对象中非空字段，空字段保留原有值。
     * </p>
     *
     * <p>HTTP 方法: PUT</p>
     * <p>请求路径: /api/v1/attributes/{attributeId}</p>
     *
     * @param attributeId 待更新属性的唯一标识符，从 URL 路径中提取
     * @param request 属性更新请求对象，包含需要修改的字段（支持部分更新），
     *                由框架进行 JSR 380 参数校验
     * @return 统一响应体 Response&lt;Void&gt;，仅包含操作成功状态，无业务数据返回
     * @throws com.example.onlinestore.exception.BizException 当指定 ID 的属性不存在时，
     *         错误码为 ATTRIBUTE_NOT_FOUND
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 当请求参数校验失败时，框架自动抛出
     */
    @PutMapping("/{attributeId}")
    public Response<Void> updateAttribute(@PathVariable("attributeId") Long attributeId,
                                                      @Valid @RequestBody UpdateAttributeRequest request) {
        attributeService.updateAttribute(attributeId,request);
        return Response.success();
    }
}
