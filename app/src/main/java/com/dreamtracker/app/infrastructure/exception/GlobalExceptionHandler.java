package com.dreamtracker.app.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
            .build();

    return new ResponseEntity<>(errorObjectToReturn, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CredentialsValidationException.class)
  public ResponseEntity<ErrorObject> handleCredentialsValidationException(
      CredentialsValidationException exception, WebRequest request) {

    var errorObjectToReturn =
        ErrorObject.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(exception.getMessage())
            .build();

    return new ResponseEntity<>(errorObjectToReturn, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorObject> handleCredentialsValidationException(
          BadCredentialsException exception, WebRequest request) {

    var errorObjectToReturn =
            ErrorObject.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(exception.getMessage())
                    .build();

    return new ResponseEntity<>(errorObjectToReturn, HttpStatus.BAD_REQUEST);
  }
}
