package com.example.onlinestore.util;

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

public class JacksonJsonUtils {
    private static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    }

    public static <T> T toObject(String json, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        return JSON_MAPPER.readValue(json, clazz);
    }

    public static <T> List<T> toList(String json, Class<T> tClass) throws IOException {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>(0);
        }
        JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
        return JSON_MAPPER.readValue(json, javaType);
    }

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

    public static String toString(Object value) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(value);
    }


}
