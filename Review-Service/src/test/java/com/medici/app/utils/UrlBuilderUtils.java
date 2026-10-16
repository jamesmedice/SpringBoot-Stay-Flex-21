package com.medici.app.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public final class UrlBuilderUtils {

    private UrlBuilderUtils() {}

    public static String buildUrl(String basePath) {
        return buildUrl(basePath, null, null);
    }

    public static String buildUrl(String basePath, Map<String, ?> pathParams) {
        return buildUrl(basePath, pathParams, null);
    }

    public static String buildUrl(String basePath, Map<String, ?> pathParams, Map<String, ?> queryParams) {
        String resolvedPath = basePath;

        // Replace {param} placeholders in the path
        if (pathParams != null && !pathParams.isEmpty()) {
            for (Map.Entry<String, ?> entry : pathParams.entrySet()) {
                resolvedPath = resolvedPath.replace("{" + entry.getKey() + "}", entry.getValue().toString());
            }
        }

        // Build query parameters if present
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(resolvedPath);
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach((key, value) -> {
                if (value != null) builder.queryParam(key, value);
            });
        }

        return builder.toUriString();
    }
}