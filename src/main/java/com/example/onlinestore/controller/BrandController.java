package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("")
    public Response<Page<Brand>> listBrands(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "visible", required = false, defaultValue = "1") Integer visible) {
        BrandListQueryOptions options = new BrandListQueryOptions();
        options.setPageNum(pageNum);
        options.setPageSize(pageSize);
        options.setVisible(visible);
        Page<Brand> brands = brandService.listBrands(options);
        return Response.success(brands);
    }

    @GetMapping("/{brandId}")
    public Response<Brand> getBrandById(@NotNull @PathVariable("brandId") Long brandId) {
        Brand brand = brandService.getBrandById(brandId);
        return Response.success(brand);
    }

    @PostMapping("")
    public Response<Brand> addBrand(@Valid @RequestBody Brand brand) {
        Brand newBrand = brandService.tianJiaPingPai(brand);
        return Response.success(newBrand);
    }

    @PutMapping("/{brandId}")
    public Response<Void> updateBrand(@NotNull @PathVariable("brandId") Long brandId,
                                      @Valid @RequestBody Brand brand) {
        brandService.updateBrand(brandId, brand);
        return Response.success();
    }

    @DeleteMapping("/{brandId}")
    public Response<String> deleteBrand(@NotNull @PathVariable("brandId") Long brandId) {
        brandService.delteBrand(brandId);
        return Response.success("Success");
    }
}
