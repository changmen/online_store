package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.entity.BrandEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.BrandMapper;
import com.example.onlinestore.service.BrandService;
import com.example.onlinestore.service.ContentValidationService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.onlinestore.utils.CommonUtils.updateFieldIfChanged;

@Service
@Validated
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private static final String DEFAULT_BRAND_LIST_QUERY_ORDERBY = "sort_score DESC";

    private static final Object BRAND_NAME_MODIFICATION_LOCK = new Object();

    private final Cache<Long, Brand> brandCache;

    private final BrandMapper brandMapper;
    private final ContentValidationService contentValidationService;

    @Override
    @Transactional(readOnly = true)
    public Brand getBrandById(@NotNull Long id) {
        Brand cached = brandCache.getIfPresent(id);
        if (cached != null) {
            return cached;
        }

        BrandEntity brandEntity = brandMapper.findById(id);
        if (brandEntity == null) {
            logger.error("brand not found, id: {}", id);
            throw new BizException(ErrorCode.BRAND_NOT_FOUND);
        }

        Brand brand = convertToBrand(brandEntity);
        brandCache.put(id, brand);
        return brand;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            brandCache.invalidate(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Brand addBrand(@NotNull @Valid Brand brand) {
        contentValidationService.validateForbiddenWords(brand.getName(), ErrorCode.BRAND_NAME_CONTAIN_SPECIAL_CHARACTER);
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
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            Brand result = convertToBrand(brandEntity);
            brandCache.put(brandEntity.getId(), result);
            return result;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(@NotNull Long id) {
        // 校验品牌是否存在
        getBrandById(id);

        synchronized (BRAND_NAME_MODIFICATION_LOCK) {
            int effectRows = brandMapper.deleteById(id);
            if (effectRows != 1) {
                logger.error("delete brand failed. because effect rows is 0. brandId:{}", id);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            brandCache.invalidate(id);
        }

    }

    @Scheduled(fixedRate = 300000)
    public void loadAllBrands() {
        logger.info("Start to load brand cache.");
        try {
            List<BrandEntity> entities = brandMapper.findAllBrands(null);
            Map<Long, Brand> newCache = new java.util.HashMap<>();
            for (BrandEntity entity : entities) {
                newCache.put(entity.getId(), convertToBrand(entity));
            }
            brandCache.invalidateAll();
            brandCache.putAll(newCache);
        } catch (Throwable t) {
            logger.error("Load brand cache failed", t);
        }
        logger.info("Complete to load brand cache, size: {}", brandCache.estimatedSize());
    }

    // 将品牌实体转换为品牌对象
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
