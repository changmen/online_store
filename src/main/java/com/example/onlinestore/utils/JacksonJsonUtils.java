package com.example.onlinestore.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * json操作简单的封装
 *
 * @author changmen.zx
 * @date 2019-11-25
 */
public class JacksonJsonUtils {
    private static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    }

    /**
     * json to pojo object
     *
     * @param json  json
     * @param clazz 目标类
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        return JSON_MAPPER.readValue(json, clazz);
    }

    /**
     * toList
     *
     * @param json
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> toList(String json, Class<T> tClass) throws IOException {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>(0);
        }
        JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
        return JSON_MAPPER.readValue(json, javaType);
    }

    /**
     * 转换成List String
     * @param json
     * @return
     * @throws IOException
     */
    public static List<String> toListString(String json) throws IOException {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>(0);
        }
        JsonNode jsonNode = JSON_MAPPER.readTree(json);
        List<String> result = new ArrayList<>();
        for(JsonNode node : jsonNode) {
            result.add(node.toString());
        }
        return result;
    }

    /**
     * object to json string
     *
     * @param value
     * @return
     * @throws JsonProcessingException
     */
    public static String toString(Object value) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(value);
    }


}
