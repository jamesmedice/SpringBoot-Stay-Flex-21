package com.medici.app.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadgeHash_URLS {

    BASE_BADGEHASHMANAGER("/badge"),
    HASH("/badge/hash");

    private String url;
}
