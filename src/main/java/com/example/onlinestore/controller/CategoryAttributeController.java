package com.example.onlinestore.controller;

import com.example.onlinestore.bean.CategoryAttribute;
import com.example.onlinestore.dto.CategoryAttributeQuery;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.CategoryAttributeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category-attributes")
public class CategoryAttributeController {

    private final CategoryAttributeService categoryAttributeService;

    public CategoryAttributeController(CategoryAttributeService categoryAttributeService) {
        this.categoryAttributeService = categoryAttributeService;
    }

    @PostMapping
    public Response<Long> addCategoryAttribute(@RequestBody CategoryAttribute categoryAttribute) {
        Long id = categoryAttributeService.addCategoryAttribute(categoryAttribute);
        return Response.success(id);
    }

    @PutMapping("/{id}")
    public Response<Boolean> updateCategoryAttribute(@PathVariable Long id, @RequestBody CategoryAttribute categoryAttribute) {
        categoryAttribute.setId(id);
        boolean result = categoryAttributeService.updateCategoryAttribute(categoryAttribute);
        return result ? Response.success(true) : Response.fail("修改失败，请检查参数");
    }

    @DeleteMapping("/{id}")
    public Response<Boolean> deleteCategoryAttribute(@PathVariable Long id) {
        boolean result = categoryAttributeService.deleteCategoryAttribute(id);
        return result ? Response.success(true) : Response.fail("删除失败，属性不存在");
    }

    @DeleteMapping("/batch")
    public Response<Boolean> batchDeleteCategoryAttributes(@RequestBody List<Long> ids) {
        boolean result = categoryAttributeService.piLiangDeleteCategoryAttributes(ids);
        return result ? Response.success(true) : Response.fail("批量删除失败，请检查参数");
    }

    @GetMapping("/{id}")
    public Response<CategoryAttribute> getCategoryAttributeById(@PathVariable Long id) {
        CategoryAttribute attribute = categoryAttributeService.getCategoryAttributeById(id);
        return attribute != null ? Response.success(attribute) : Response.fail("属性不存在");
    }

    @GetMapping("/category/{categoryId}")
    public Response<List<CategoryAttribute>> getCategoryAttributesByCategoryId(@PathVariable Long categoryId) {
        List<CategoryAttribute> list = categoryAttributeService.getCategoryAttributesByCategoryId(categoryId);
        return Response.success(list);
    }

    @GetMapping("/page")
    public Response<Map<String, Object>> pageCategoryAttributes(CategoryAttributeQuery query) {
        Map<String, Object> result = categoryAttributeService.pageCategoryAttributes(query);
        return Response.success(result);
    }

    @GetMapping("/check-name")
    public Response<Boolean> checkNameExists(
            @RequestParam Long categoryId,
            @RequestParam String name,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = categoryAttributeService.checkNameExists(categoryId, name, excludeId);
        return Response.success(exists);
    }
}
