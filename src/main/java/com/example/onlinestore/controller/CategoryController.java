package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/{categoryId}")
    public Response<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return Response.success(category);
    }

    @GetMapping("/{categoryId}/path")
    public Response<String> getCategoryPath(@PathVariable("categoryId") Long categoryId) {
        String categoryPath = categoryService.getCategoryPath(categoryId);
        return Response.success(categoryPath);
    }

    @GetMapping("/{categoryId}/second-path")
    public Response<String> getCategoryFirstPath(@PathVariable("categoryId") Long categoryId) {
        String categoryPath = categoryService.getCategoryPath(categoryId);
        if (StringUtils.isNotBlank(categoryPath)) {
            return Response.success(categoryPath.split("->")[1]);
        }
        return Response.success("");
    }
}
