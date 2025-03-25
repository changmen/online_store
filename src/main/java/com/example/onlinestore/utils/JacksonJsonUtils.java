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
     * 将JSON字符串反序列化为指定类型的对象实例。
     * @param json 需要解析的JSON格式字符串
     * @param clazz 目标对象的Class类型，用于确定反序列化的目标类型
     * @return 根据JSON数据创建的指定类型对象实例
     * @throws IOException 当JSON解析过程中发生I/O错误或格式错误时抛出
     */
    public static <T> T toObject(String json, Class<T> clazz) throws IOException {

        if (StringUtils.isBlank(json)) {
            return null;
        }

        return JSON_MAPPER.readValue(json, clazz);
    }

    /**
     * 将JSON字符串转换为指定类型的List对象
     * @param json 需要解析的JSON格式字符串
     * @param tClass 列表元素类型对应的Class对象
     * @return 解析后的List对象，元素类型与tClass一致
     * @throws IOException JSON解析或反序列化过程中发生错误时抛出
     */
    public static <T> List<T> toList(String json, Class<T> tClass) throws IOException {

        if (StringUtils.isBlank(json)) {
            return new ArrayList<>(0);
        }
        JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
        return JSON_MAPPER.readValue(json, javaType);
    }


    /**
     * 将给定的JSON字符串转换为字符串列表。
     *
     * 该函数接收一个JSON格式的字符串，并将其解析为一个包含字符串元素的列表。
     * 通常用于处理JSON数组，其中每个元素都是字符串类型。
     *
     * @param json 要解析的JSON字符串，通常是一个JSON数组格式的字符串。
     * @return 返回一个包含解析后的字符串元素的列表。如果JSON字符串无法解析或格式不正确，可能会抛出异常。
     * @throws IOException 如果解析过程中发生I/O错误，或者JSON字符串格式不正确，将抛出此异常。
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
     * 将给定的对象转换为JSON格式的字符串。
     *
     * 该函数使用Jackson库的ObjectMapper将Java对象序列化为JSON字符串。
     * 如果对象无法被序列化，将抛出JsonProcessingException异常。
     *
     * @param value 需要转换为JSON字符串的对象，可以是任意Java对象。
     * @return 返回表示对象的JSON格式字符串。
     * @throws JsonProcessingException 如果对象无法被序列化为JSON字符串时抛出此异常。
     */
    public static String toString(Object value) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(value);
    }


}
