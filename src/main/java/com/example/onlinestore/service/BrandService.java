package com.example.onlinestore.service;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface BrandService {
    /**
 * Retrieves a brand entity by its unique identifier.
 *
 * @param id the unique identifier of the brand; must not be null
 * @return the corresponding Brand object, or null if no brand with the given id exists
 */
    Brand getBrandById(@NotNull Long id);

    /****
 * Returns a paginated list of brands based on the specified query options.
 *
 * @param options query options including pagination, filtering, and sorting criteria; must not be null
 * @return a page containing the list of brands and pagination details
 */
    Page<Brand> listBrands(@NotNull @Valid BrandListQueryOptions options);

    /**
 * Adds a new brand entity to the system.
 *
 * @param brand the brand entity to add; must be valid and not null
 * @return the created brand entity, including system-generated fields such as the ID
 */
    Brand tianJiaPingPai(@NotNull @Valid Brand brand);

    /**
 * Deletes the brand identified by the given ID.
 *
 * @param id the unique identifier of the brand to delete; must not be null
 */
    void delteBrand(@NotNull  Long id);

    /**
 * Updates the brand information for the specified ID using the provided brand data.
 *
 * @param id the unique identifier of the brand to update; must not be null
 * @param brand the new brand data; must be valid and not null
 * @throws com.example.onlinestore.exceptions.BizException if the brand with the given ID does not exist or if the brand name duplicates an existing one
 */
    void updateBrand(@NotNull Long id, @NotNull @Valid Brand brand);


}
