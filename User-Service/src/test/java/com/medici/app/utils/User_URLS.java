package com.medici.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum User_URLS {

    BASE_USER("/users"), SAVE("/users/save"), UPDATE("/users/update/{id}"), FIND_BY_ID("/users/{id}"), FIND_All("/users/all"), PAGE_All("/users/page/all");

    private String url;
}
