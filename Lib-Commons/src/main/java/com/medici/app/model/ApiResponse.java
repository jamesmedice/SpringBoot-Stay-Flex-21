package com.medici.app.model;

import lombok.Data;

@Data
public class ApiResponse<T extends ResponsePayload> {

    T response;
}
