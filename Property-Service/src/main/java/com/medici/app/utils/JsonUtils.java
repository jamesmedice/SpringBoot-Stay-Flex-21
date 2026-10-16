package com.medici.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

	public static <T> T jsonStringToObject(String content, Class<T> clazz) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			T obj = objMapper.readValue(content, clazz);
			return obj;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static <T> T jsonStringToObject(InputStream content, Class<T> clazz) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			T obj = objMapper.readValue(content, clazz);
			return obj;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static String serializeAsJsonString(Object object) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			StringWriter sw = new StringWriter();
			objMapper.writeValue(sw, object);
			return sw.toString();
		} catch (IOException e) {
			return null;
		}
	}
}
