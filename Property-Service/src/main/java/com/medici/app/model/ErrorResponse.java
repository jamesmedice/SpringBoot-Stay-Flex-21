package com.medici.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String code;

    private String message;

}

