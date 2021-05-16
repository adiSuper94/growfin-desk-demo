package com.adiSuper.desk.config;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
    return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(DuplicateKeyException.class)
  protected ResponseEntity<Object> handleDuplicateKey(DuplicateKeyException ex) {
    String errorMessage = ex.getCause().getMessage();
    return buildResponseEntity(HttpStatus.CONFLICT, errorMessage);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String errorMessage = ex.getCause().getMessage();
    return buildResponseEntity(status, errorMessage);
  }

  private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
    HashMap<String, Object> errorObject = new HashMap<>();
    errorObject.put("message", message);
    errorObject.put("status", status);
    errorObject.put("status_code", status.value());
    errorObject.put("timestamp", new Date().toString());
    return new ResponseEntity<>(errorObject, status);
  }
}
