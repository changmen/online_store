package com.example.onlinestore.mapper;

import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.entity.BrandEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BrandMapper {

    /**
 * Inserts a new brand record into the database.
 *
 * @param brandEntity the brand entity containing the details to insert
 * @return the number of rows affected (1 if successful, 0 if insertion failed)
 */
    int insert(BrandEntity brandEntity);

    /**
 * Retrieves a brand entity by its unique identifier.
 *
 * @param id the unique identifier of the brand
 * @return the matching BrandEntity, or null if no brand with the given ID exists
 */
    BrandEntity findById(Long id);

    /**
 * Retrieves a brand entity by its exact name.
 *
 * @param name the exact name of the brand to search for
 * @return the matching BrandEntity, or null if no brand with the given name exists
 */
    BrandEntity findByName(String name);

    /**
 * Retrieves a list of brand entities matching the specified query options.
 *
 * @param options query options for filtering, sorting, and pagination
 * @return a list of matching brand entities, or an empty list if none are found
 */
    List<BrandEntity> findAllBrands(@Param("options") BrandListQueryOptions options);

    /**
 * Deletes a brand record by its unique ID.
 *
 * @param id the unique identifier of the brand to delete
 * @return the number of rows affected; 1 if the record was deleted, 0 if no matching record was found
 */
    int deleteById(Long id);

    /**
 * Updates an existing brand record with the non-null fields from the provided entity.
 *
 * The brand entity must include a valid ID to identify the record to update. Only fields that are not null will be updated in the database.
 *
 * @param brandEntity the brand entity containing the ID and fields to update
 * @return the number of affected rows; 1 if the update was successful, or 0 if no record was updated (due to non-existent ID or unchanged data)
 */
    int update(BrandEntity brandEntity);


}
