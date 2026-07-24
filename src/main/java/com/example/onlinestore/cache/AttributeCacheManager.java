package com.example.onlinestore.cache;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class AttributeCacheManager {

    private final Cache<Long, Attribute> attributeByIdCache;
    private final Cache<Long, AttributeValue> attributeValueByIdCache;
    private final Cache<Long, List<AttributeValue>> valuesByAttributeIdCache;
    private final Map<Long, Set<Long>> attributeValueIndex = new ConcurrentHashMap<>();

    public AttributeCacheManager() {
        this.attributeByIdCache = Caffeine.newBuilder()
                .maximumSize(CacheConstants.ATTRIBUTE_MAX_SIZE)
                .expireAfterWrite(CacheConstants.ATTRIBUTE_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
        RemovalListener<Long, AttributeValue> valueEvictionListener = (key, value, cause) -> {
            if (value != null) {
                Set<Long> valueIds = attributeValueIndex.get(value.getAttributeId());
                if (valueIds != null) {
                    valueIds.remove(key);
                    if (valueIds.isEmpty()) {
                        attributeValueIndex.remove(value.getAttributeId());
                    }
                }
            }
        };
        this.attributeValueByIdCache = Caffeine.newBuilder()
                .maximumSize(CacheConstants.ATTRIBUTE_VALUE_MAX_SIZE)
                .expireAfterWrite(CacheConstants.ATTRIBUTE_TTL_MINUTES, TimeUnit.MINUTES)
                .removalListener(valueEvictionListener)
                .recordStats()
                .build();
        this.valuesByAttributeIdCache = Caffeine.newBuilder()
                .maximumSize(CacheConstants.ATTRIBUTE_VALUES_BY_ID_MAX_SIZE)
                .expireAfterWrite(CacheConstants.ATTRIBUTE_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    public void putAttribute(Attribute attribute) {
        attributeByIdCache.put(attribute.getId(), attribute);
    }

    public void invalidateAttribute(Long id) {
        attributeByIdCache.invalidate(id);
    }

    public void invalidateAllByAttributeId(Long attributeId) {
        attributeByIdCache.invalidate(attributeId);
        valuesByAttributeIdCache.invalidate(attributeId);
        Set<Long> valueIds = attributeValueIndex.remove(attributeId);
        if (valueIds != null) {
            valueIds.forEach(attributeValueByIdCache::invalidate);
        }
    }

    public Attribute getOrLoadAttribute(Long id, Function<Long, Attribute> loader) {
        return attributeByIdCache.get(id, loader);
    }

    public AttributeValue getOrLoadAttributeValue(Long id, Function<Long, AttributeValue> loader) {
        return attributeValueByIdCache.get(id, key -> {
            AttributeValue value = loader.apply(key);
            if (value != null) {
                indexValueId(value.getAttributeId(), value.getId());
            }
            return value;
        });
    }

    public List<AttributeValue> getOrLoadValuesByAttributeId(Long attributeId, Function<Long, List<AttributeValue>> loader) {
        return valuesByAttributeIdCache.get(attributeId, key -> {
            List<AttributeValue> values = loader.apply(key);
            if (values != null) {
                for (AttributeValue v : values) {
                    indexValueId(attributeId, v.getId());
                }
            }
            return values;
        });
    }

    private void indexValueId(Long attributeId, Long valueId) {
        attributeValueIndex.computeIfAbsent(attributeId, k -> ConcurrentHashMap.newKeySet()).add(valueId);
    }

    public List<Attribute> batchGetAttributes(List<Long> ids, Function<List<Long>, List<Attribute>> loader) {
        return batchGet(ids, attributeByIdCache, a -> a.getId(), loader);
    }

    public Map<Long, AttributeValue> batchGetAttributeValues(List<Long> ids, Function<List<Long>, List<AttributeValue>> loader) {
        Map<Long, AttributeValue> result = new HashMap<>(ids.size());
        for (AttributeValue av : batchGet(ids, attributeValueByIdCache, AttributeValue::getId, loader)) {
            result.put(av.getId(), av);
        }
        return result;
    }

    public Map<Long, List<AttributeValue>> batchGetValuesByAttributeIds(List<Long> attributeIds, Function<List<Long>, Map<Long, List<AttributeValue>>> loader) {
        return batchGetMap(attributeIds, valuesByAttributeIdCache, Collections.emptyList(), loader);
    }

    private <V> List<V> batchGet(List<Long> ids, Cache<Long, V> cache, Function<V, Long> keyExtractor, Function<List<Long>, ? extends Collection<V>> loader) {
        List<V> result = new ArrayList<>(ids.size());
        List<Long> missedIds = new ArrayList<>();
        for (Long id : ids) {
            V cached = cache.getIfPresent(id);
            if (cached != null) {
                result.add(cached);
            } else {
                missedIds.add(id);
            }
        }
        if (!missedIds.isEmpty()) {
            for (V item : loader.apply(missedIds)) {
                cache.put(keyExtractor.apply(item), item);
                result.add(item);
            }
        }
        return result;
    }

    private <V> Map<Long, V> batchGetMap(List<Long> ids, Cache<Long, V> cache, V defaultValue, Function<List<Long>, Map<Long, V>> loader) {
        Map<Long, V> result = new HashMap<>(ids.size());
        List<Long> missedIds = new ArrayList<>();
        for (Long id : ids) {
            V cached = cache.getIfPresent(id);
            if (cached != null) {
                result.put(id, cached);
            } else {
                missedIds.add(id);
            }
        }
        if (!missedIds.isEmpty()) {
            Map<Long, V> loaded = loader.apply(missedIds);
            for (Long id : missedIds) {
                V value = loaded.getOrDefault(id, defaultValue);
                cache.put(id, value);
                result.put(id, value);
            }
        }
        return result;
    }
}
