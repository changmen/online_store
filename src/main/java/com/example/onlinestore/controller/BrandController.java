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

    /**
     * Returns a paginated list of brands, optionally filtered by visibility.
     *
     * @param pageNum the page number to retrieve (default is 1)
     * @param pageSize the number of brands per page (default is 10)
     * @param visible filter for brand visibility status (default is 1)
     * @return a response containing a page of brands
     */
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

    /**
     * Retrieves a brand by its unique identifier.
     *
     * @param brandId the ID of the brand to retrieve; must not be null
     * @return a successful response containing the requested brand
     */
    @GetMapping("/{brandId}")
    public Response<Brand> getBrandById(@NotNull @PathVariable("brandId") Long brandId) {
        Brand brand = brandService.getBrandById(brandId);
        return Response.success(brand);
    }

    /**
     * Adds a new brand using the provided brand data.
     *
     * @param brand the brand information to add
     * @return a success response containing the newly created brand
     */
    @PostMapping("")
    public Response<Brand> addBrand(@Valid @RequestBody Brand brand) {
        Brand newBrand = brandService.tianJiaPingPai(brand);
        return Response.success(newBrand);
    }

    /**
     * Updates an existing brand with the specified ID using the provided brand data.
     *
     * @param brandId the ID of the brand to update; must not be null
     * @param brand the updated brand information
     * @return a success response with no content
     */
    @PutMapping("/{brandId}")
    public Response<Void> updateBrand(@NotNull @PathVariable("brandId") Long brandId,
                                      @Valid @RequestBody Brand brand) {
        brandService.updateBrand(brandId, brand);
        return Response.success();
    }

    /**
     * Deletes a brand by its ID.
     *
     * @param brandId the ID of the brand to delete; must not be null
     * @return a success response with a confirmation message
     */
    @DeleteMapping("/{brandId}")
    public Response<String> deleteBrand(@NotNull @PathVariable("brandId") Long brandId) {
        brandService.delteBrand(brandId);
        return Response.success("Success");
    }
}
