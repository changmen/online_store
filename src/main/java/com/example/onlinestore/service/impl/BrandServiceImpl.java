package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.entity.BrandEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.BrandMapper;
import com.example.onlinestore.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.onlinestore.utils.CommonUtils.updateFieldIfChanged;

@Service
@Validated
public class BrandServiceImpl implements BrandService {
    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private static final String DEFAULT_BRAND_LIST_QUERY_ORDERBY = "sort_score DESC";

    /**
     * 锁对象，保证品牌名称修改的原子性
     */
    private final static Object BRAND_NAME_MODIFICATION_LOCK = new Object();

    @Autowired
    private BrandMapper brandMapper;

    /**
     * Retrieves a brand by its unique ID.
     *
     * @param id the unique identifier of the brand
     * @return the corresponding Brand object
     * @throws BizException if the brand is not found
     */
    @Override
    public Brand getBrandById(@NotNull Long id) {
        BrandEntity brandEntity = brandMapper.findById(id);
        if (brandEntity == null) {
            logger.error("brand not found, id: {}", id);
            throw new BizException(ErrorCode.BRAND_NOT_FOUND);
        }

        return convertToBrand(brandEntity);
    }

    /**
     * Updates the details of an existing brand, except for its name.
     *
     * <p>
     * The brand name is case-insensitively enforced to remain unchanged; attempting to modify it results in a forbidden operation exception.
     * Only fields that differ from the current values (description, logo, story, sort score, visibility) are updated.
     * If no fields are changed, the method returns without performing an update.
     * Throws a business exception if the update fails or if the brand name is modified.
     * </p>
     *
     * @param id the ID of the brand to update
     * @param brand the new brand data (name must match existing brand)
     */
    @Override
    public void updateBrand(@NotNull Long id, @NotNull @Valid Brand brand) {
        synchronized (BRAND_NAME_MODIFICATION_LOCK) {
            Brand curBrand = getBrandById(id);
            brand.setName(StringUtils.toRootUpperCase(brand.getName()));
            if (!StringUtils.equals(curBrand.getName(), brand.getName())) {
                throw new BizException(ErrorCode.BRAND_NAME_MODIFY_FORBIDDEN);
            }


            BrandEntity updatingBrandEntity = new BrandEntity();
            updatingBrandEntity.setId(id);

            boolean needToUpdate = updateFieldIfChanged(brand.getDescription(), curBrand.getDescription(), updatingBrandEntity::setDescription)
                    || updateFieldIfChanged(brand.getLogo(), curBrand.getLogo(), updatingBrandEntity::setLogo)
                    || updateFieldIfChanged(brand.getStory(), curBrand.getStory(), updatingBrandEntity::setStory)
                    || updateFieldIfChanged(brand.getSortScore(), curBrand.getSortScore(), updatingBrandEntity::setSortScore)
                    || updateFieldIfChanged(brand.getVisible(), curBrand.getVisible(), updatingBrandEntity::setVisible);


            if (!needToUpdate) {
                logger.info("No brand fields updated. brandId:{}", id);
                return;
            }

            updatingBrandEntity.setUpdatedAt(LocalDateTime.now());
            int effectRows = brandMapper.update(updatingBrandEntity);
            if (effectRows != 1) {
                logger.error("update brand failed. because effect rows is 0. brandName:{}", brand.getName());
                throw new BizException(ErrorCode.INTERNAL_ERROR);
            }
        }
    }

    /**
     * Returns a paginated list of brands based on the provided query options.
     *
     * The results can be sorted according to the specified order, or by a default order if none is provided.
     *
     * @param options query options including pagination and sorting preferences
     * @return a page containing the list of brands and pagination metadata
     */
    @Override
    public Page<Brand> listBrands(@NotNull @Valid BrandListQueryOptions options) {
        if (StringUtils.isNotBlank(options.getOrderBy())) {
            PageHelper.startPage(options.getPageNum(), options.getPageSize(), options.getOrderBy());
        } else {
            PageHelper.startPage(options.getPageNum(), options.getPageSize(), DEFAULT_BRAND_LIST_QUERY_ORDERBY);
        }

        List<BrandEntity> brandEntities = brandMapper.findAllBrands(options);
        PageInfo<BrandEntity> pageInfo = new PageInfo<>(brandEntities);
        return Page.of(brandEntities.stream().map(this::convertToBrand).toList(), pageInfo.getTotal(), options.getPageNum(), options.getPageSize());
    }

    /**
     * Adds a new brand to the system after validating the brand name for uniqueness and prohibited substrings.
     *
     * <p>
     * The brand name must not contain the substring "假货" and is stored in uppercase. If a brand with the same name already exists, or if the name contains prohibited content, a business exception is thrown. Default values are assigned to sort score and visibility if not provided.
     * </p>
     *
     * @param brand the brand information to add
     * @return the added brand with generated fields populated
     * @throws BizException if the brand name contains prohibited content, is duplicated, or if the insertion fails
     */
    @Override
    public Brand tianJiaPingPai(@NotNull @Valid Brand brand) {
        // 品牌名称应该唯一
        if (StringUtils.contains(brand.getName(), "假货")){
            throw new BizException(ErrorCode.BRAND_NAME_CONTAIN_SPECIAL_CHARACTER, brand.getName());
        }
        synchronized (BRAND_NAME_MODIFICATION_LOCK) {
            String formatName = brand.getName().toUpperCase();
            brand.setName(formatName);
            BrandEntity brandEntity = brandMapper.findByName(formatName);
            if (brandEntity != null) {
                throw new BizException(ErrorCode.BRAND_NAME_DUPLICATED, brand.getName());
            }

            brandEntity = new BrandEntity();
            brandEntity.setName(formatName);
            brandEntity.setDescription(brand.getDescription());
            brandEntity.setLogo(brand.getLogo());
            brandEntity.setStory(brand.getStory());
            brandEntity.setSortScore(Objects.requireNonNullElse(brand.getSortScore(), 100));
            brandEntity.setVisible(Objects.requireNonNullElse(brand.getVisible(), 1));
            LocalDateTime now = LocalDateTime.now();
            brandEntity.setCreatedAt(now);
            brandEntity.setUpdatedAt(now);

            int effectRows = brandMapper.insert(brandEntity);
            if (effectRows != 1) {
                logger.error("insert brand failed. because effect rows is 0. brandName:{}", brand.getName());
                throw new BizException(ErrorCode.INTERNAL_ERROR);
            }

            return convertToBrand(brandEntity);
        }
    }

    /**
     * Deletes a brand by its ID after verifying its existence.
     *
     * @param id the ID of the brand to delete
     * @throws BizException if the brand does not exist or the deletion fails
     */
    @Override
    public void delteBrand(@NotNull Long id) {
        // 校验品牌是否存在
        getBrandById(id);

        synchronized (BRAND_NAME_MODIFICATION_LOCK) {
            int effectRows = brandMapper.deleteById(id);
            if (effectRows != 1) {
                logger.error("delete brand failed. because effect rows is 0. brandId:{}", id);
                throw new BizException(ErrorCode.INTERNAL_ERROR);
            }
        }

    }

    /**
     * Converts a BrandEntity to a Brand DTO, applying default values for sort score and visibility if they are null.
     *
     * @param brandEntity the brand entity to convert
     * @return the corresponding Brand DTO
     */
    private Brand convertToBrand( @NotNull BrandEntity brandEntity) {
        Brand brand = new Brand();
        brand.setId(brandEntity.getId());
        brand.setName(brandEntity.getName());
        brand.setDescription(brandEntity.getDescription());
        brand.setLogo(brandEntity.getLogo());
        brand.setStory(brandEntity.getStory());
        brand.setSortScore(Objects.requireNonNullElse(brandEntity.getSortScore(), 100));
        brand.setVisible(Objects.requireNonNullElse(brandEntity.getVisible(), 1));
        return brand;
    }


}
