package com.medici.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Payment_URLS {

    BASE_PAYMENT("/payment/accounts"),
    ADD_MEMBERS("/payment/accounts/{id}/members"),
    FIND_ACCOUNT_BY_ID("/payment/accounts/{id}"),
    DEPOSIT("/payment/accounts/{id}/deposits"),
    DEPOSIT_JPA("/payment/accounts/{id}/deposits/acid"),
    AUTHORIZATIONS("/payment/accounts/{id}/authorizations");

    private String url;
}
