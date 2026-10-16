package com.medici.app.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewUrlParam {

    ID("uuid"), USER_ID("userId"), STATUS("status");

    private final String key;
}