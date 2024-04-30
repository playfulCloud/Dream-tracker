package com.dreamtracker.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorObject> handleEntityNotFoundException(
      EntityNotFoundException exception, WebRequest request) {

    var errorObjectToReturn =
        ErrorObject.builder()
            .code(HttpStatus.NOT_FOUND.value())
            .message(exception.getMessage())
            .details(exception.getDetails())
            .build();

    return new ResponseEntity<>(errorObjectToReturn, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntitySaveException.class)
  public ResponseEntity<ErrorObject> handleEntitySaveException(
      EntitySaveException exception, WebRequest request) {

    var errorObjectToReturn =
        ErrorObject.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(exception.getMessage())
            .details(exception.getDetails())
            .build();

    return new ResponseEntity<>(errorObjectToReturn, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
