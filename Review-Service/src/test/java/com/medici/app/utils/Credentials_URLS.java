package com.medici.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Credentials_URLS {

    BASE_CREDENTIALS("/credentials"),
    MESH("/credentials/mesh");

    private String url;
}
