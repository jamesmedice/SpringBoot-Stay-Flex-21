package com.medici.app.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserUrlParam {

    ID("id"), USERNAME("username"), ACTIVE("active"), ROLE("role");

    private final String key;
}