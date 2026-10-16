package com.medici.app.component;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.medici.app.model.ErrorResponse;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        ErrorResponse error = getError(ex, HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse error = getError(ex, HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
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

    public ErrorResponse getError(Exception ex, String code) {
        log.error(ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse();
        error.setCode(code);
        error.setMessage(ex.getMessage());
        return error;
    }

}
