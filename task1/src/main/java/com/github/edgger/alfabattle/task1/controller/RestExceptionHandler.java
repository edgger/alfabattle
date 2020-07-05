package com.github.edgger.alfabattle.task1.controller;

import com.github.edgger.alfabattle.task1.exceptions.AtmNotFoundException;
import com.github.edgger.alfabattle.task1.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AtmNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleAtmNotFoundException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("atm not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> defaultHandleException(Exception ex, WebRequest request) {
        log.error("Something went wrong", ex);
        Throwable rootCause = NestedExceptionUtils.getMostSpecificCause(ex);
        ErrorResponse error = new ErrorResponse(rootCause.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
