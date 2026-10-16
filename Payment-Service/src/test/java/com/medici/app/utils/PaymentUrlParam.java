package com.medici.app.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentUrlParam {

    ID("id"), USER_ID("userId");

    private final String key;
}