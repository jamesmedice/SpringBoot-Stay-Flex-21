package com.medici.app.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String code;
}
