package com.medici.app.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenChain_URLS {

    BASE_TOKENCHAIN("/token"),
    CODE("/token/code");

    private String url;
}
