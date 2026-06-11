package com.example.onlinestore.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JacksonJsonUtils 类提供了与 Jackson 库相关的 JSON 处理工具方法。
 * 该类主要用于简化 JSON 的序列化和反序列化操作，通常用于将 Java 对象转换为 JSON 字符串，
 * 或者将 JSON 字符串转换为 Java 对象。
 *
 * 该类可能包含静态方法，以便在不实例化类的情况下直接调用相关工具方法。
 */
public class JacksonJsonUtils {
    private static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        JSON_MAPPER.registerModule(new JavaTimeModule());
        JSON_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 将JSON字符串转换为指定类型的Java对象。
     *
     * @param <T> 目标对象的类型参数，表示转换后的对象类型。
     * @param json 要转换的JSON字符串，不能为null。
     * @param clazz 目标对象的Class对象，用于确定转换后的对象类型。
     * @return 转换后的Java对象，类型为T。
     * @throws IOException 如果JSON字符串解析失败或转换过程中发生I/O错误。
     * @throws IllegalArgumentException 如果JSON字符串为空或目标对象类型为空。
     */
    public static <T> T toObject(String json, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(json)) {
            throw new IllegalArgumentException("json string is empty");
        }

        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }

        return JSON_MAPPER.readValue(json, clazz);
    }

    /**
     * 将JSON字符串转换为指定类型的List。
     *
     * @param <T> 目标列表元素的类型
     * @param json 要解析的JSON字符串，不能为空或空白
     * @param tClass 目标列表元素的Class对象，不能为null
     * @return 包含解析结果的List对象
     * @throws IOException 如果JSON解析过程中发生I/O错误
     * @throws IllegalArgumentException 如果json字符串为空或tClass为null
     */
    public static <T> List<T> toList(String json, Class<T> tClass) throws IOException {
        if (StringUtils.isBlank(json)) {
            throw new IllegalArgumentException("json string is empty");
        }

        if (tClass == null) {
            throw new IllegalArgumentException("class is null");
        }
        JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
        return JSON_MAPPER.readValue(json, javaType);
    }

    /**
     * 将JSON字符串转换为字符串列表。
     *
     * 该函数接收一个JSON格式的字符串，将其解析为JsonNode对象，然后遍历JsonNode中的每个元素，
     * 将其转换为字符串并添加到结果列表中。如果输入的JSON字符串为空或空白，将抛出IllegalArgumentException。
     *
     * @param json 要解析的JSON字符串，不能为空或空白。
     * @return 包含JSON字符串中所有元素的字符串列表。
     * @throws IOException 如果解析JSON字符串时发生I/O错误。
     * @throws IllegalArgumentException 如果输入的JSON字符串为空或空白。
     */
    public static List<String> toListString(String json) throws IOException {
        if (StringUtils.isBlank(json)) {
            throw new IllegalArgumentException("json string is empty");
        }

        JsonNode jsonNode = JSON_MAPPER.readTree(json);
        List<String> result = new ArrayList<>();
        for(JsonNode node : jsonNode) {
            result.add(node.asText());
        }
        return result;
    }

    /**
     * 将给定的对象转换为JSON格式的字符串。
     *
     * @param value 需要转换为JSON字符串的对象，不能为null。
     * @return 返回表示对象的JSON字符串。
     * @throws JsonProcessingException 如果对象无法被序列化为JSON字符串时抛出。
     * @throws IllegalArgumentException 如果传入的value为null时抛出。
     */
    public static String toString(Object value) throws JsonProcessingException {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        return JSON_MAPPER.writeValueAsString(value);
    }

}
