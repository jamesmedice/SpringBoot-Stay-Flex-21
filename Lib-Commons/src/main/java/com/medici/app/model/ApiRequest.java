package com.medici.app.model;

import lombok.Data;

@Data
public class ApiRequest<T extends RequestPayload> {

    T data;
}
