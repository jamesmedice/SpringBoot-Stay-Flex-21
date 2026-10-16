package com.medici.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.StringWriter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static <T> T jsonStringToObject(String content, Class<T> clazz) throws IOException {
        T obj = null;
        ObjectMapper objMapper = new ObjectMapper();
        obj = objMapper.readValue(content, clazz);
        return obj;
    }


    public static String serializeAsJsonString(Object object) {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            objMapper.registerModule(new JavaTimeModule());
            objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            StringWriter sw = new StringWriter();
            objMapper.writeValue(sw, object);
            return sw.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
