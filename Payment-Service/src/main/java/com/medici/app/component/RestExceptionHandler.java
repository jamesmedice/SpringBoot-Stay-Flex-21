package com.medici.app.component;

import com.medici.app.exceptions.*;
import com.medici.app.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        ErrorResponse error = getError(ex, HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestClientException.class)
    protected ResponseEntity<Object> handleRestClientException(RestClientException ex) {
        ErrorResponse error = getError(ex, HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse error = getError(ex, ex.getMessage());
        return new ResponseEntity(error, ex.getStatusCode());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    ResponseEntity<?> insufficient(RuntimeException ex) {
        ErrorResponse error = getError(ex, ex.getMessage());
        return ResponseEntity.status(402).body(error);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    ResponseEntity<?> missing(RuntimeException ex) {
        ErrorResponse error = getError(ex, ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler({IdempotencyConflictException.class, IllegalArgumentException.class})
    ResponseEntity<?> bad(RuntimeException ex) {
        ErrorResponse error = getError(ex, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConcurrentAuthorizationException.class)
    ResponseEntity<?> conflict(RuntimeException ex) {
        ErrorResponse error = getError(ex, ex.getMessage());
        return ResponseEntity.status(409).body(error);
    }

    public ErrorResponse getError(Exception ex, String code) {
        log.error(ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse();
        error.setCode(code);
        error.setMessage(ex.getMessage());
        return error;
    }

}
