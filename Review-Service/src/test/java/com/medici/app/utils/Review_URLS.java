package com.medici.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Review_URLS {

    BASE_REVIEWS("/reviews"),
    SAVE("/reviews/save"),
    UPDATE("/reviews/update/{uuid}"),
    DELETE("/reviews/delete/{uuid}"),
    FIND_BY_USER_ID("/reviews/users/{userId}"),
    FIND_BY_STATUS("/reviews/status/{status}"),
    SLICE("/reviews/slice"),
    ALL("/reviews/all");

    private String url;
}