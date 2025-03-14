package com.example.onlinestore.controller;

import com.example.onlinestore.bean.CategoryAttribute;
import com.example.onlinestore.dto.CategoryAttributeQuery;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.CategoryAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类目属性控制器
 */
@RestController
@RequestMapping("/api/v1/category-attributes")
public class CategoryAttributeController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryAttributeController.class);

    @Autowired
    private CategoryAttributeService categoryAttributeService;

    /**
     * 添加类目属性
     *
     * @param categoryAttribute 类目属性
     * @return 响应结果
     */
    @PostMapping
    public Response<Long> addCategoryAttribute(@RequestBody CategoryAttribute categoryAttribute) {
        try {
            Long id = categoryAttributeService.addCategoryAttribute(categoryAttribute);
            return Response.success(id);
        } catch (IllegalArgumentException e) {
            logger.error("添加类目属性失败: {}", e.getMessage());
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            logger.error("添加类目属性异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 修改类目属性
     *
     * @param id 类目属性ID
     * @param categoryAttribute 类目属性
     * @return 响应结果
     */
    @PutMapping("/{id}")
    public Response<Boolean> updateCategoryAttribute(@PathVariable Long id, @RequestBody CategoryAttribute categoryAttribute) {
        try {
            categoryAttribute.setId(id);
            boolean result = categoryAttributeService.updateCategoryAttribute(categoryAttribute);
            return result ? Response.success(true) : Response.fail("修改失败，请检查参数");
        } catch (Exception e) {
            logger.error("修改类目属性异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 删除类目属性
     *
     * @param id 类目属性ID
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    public Response<Boolean> deleteCategoryAttribute(@PathVariable Long id) {
        try {
            boolean result = categoryAttributeService.deleteCategoryAttribute(id);
            return result ? Response.success(true) : Response.fail("删除失败，属性不存在");
        } catch (Exception e) {
            logger.error("删除类目属性异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 批量删除类目属性
     *
     * @param ids 类目属性ID列表
     * @return 响应结果
     */
    @DeleteMapping("/batch")
    public Response<Boolean> batchDeleteCategoryAttributes(@RequestBody List<Long> ids) {
        try {
            boolean result = categoryAttributeService.piLiangDeleteCategoryAttributes(ids);
            return result ? Response.success(true) : Response.fail("批量删除失败，请检查参数");
        } catch (Exception e) {
            logger.error("批量删除类目属性异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 根据ID获取类目属性
     *
     * @param id 类目属性ID
     * @return 响应结果
     */
    @GetMapping("/{id}")
    public Response<CategoryAttribute> getCategoryAttributeById(@PathVariable Long id) {
        try {
            CategoryAttribute attribute = categoryAttributeService.getCategoryAttributeById(id);
            return attribute != null ? Response.success(attribute) : Response.fail("属性不存在");
        } catch (Exception e) {
            logger.error("获取类目属性异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 根据类目ID获取类目属性列表
     *
     * @param categoryId 类目ID
     * @return 响应结果
     */
    @GetMapping("/category/{categoryId}")
    public Response<List<CategoryAttribute>> getCategoryAttributesByCategoryId(@PathVariable Long categoryId) {
        try {
            List<CategoryAttribute> list = categoryAttributeService.getCategoryAttributesByCategoryId(categoryId);
            return Response.success(list);
        } catch (Exception e) {
            logger.error("获取类目属性列表异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 分页查询类目属性
     *
     * @param query 查询参数
     * @return 响应结果
     */
    @GetMapping("/page")
    public Response<Map<String, Object>> pageCategoryAttributes(CategoryAttributeQuery query) {
        try {
            Map<String, Object> result = categoryAttributeService.pageCategoryAttributes(query);
            return Response.success(result);
        } catch (Exception e) {
            logger.error("分页查询类目属性异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }

    /**
     * 检查类目属性名称是否已存在
     *
     * @param categoryId 类目ID
     * @param name 属性名称
     * @param excludeId 排除的属性ID（可选）
     * @return 响应结果
     */
    @GetMapping("/check-name")
    public Response<Boolean> checkNameExists(
            @RequestParam Long categoryId,
            @RequestParam String name,
            @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = categoryAttributeService.checkNameExists(categoryId, name, excludeId);
            return Response.success(exists);
        } catch (Exception e) {
            logger.error("检查类目属性名称异常", e);
            return Response.fail("系统异常，请稍后重试");
        }
    }
} 