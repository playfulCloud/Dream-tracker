package com.dreamtracker.app.infrastructure.exception;

public class ExceptionMessages {

    private ExceptionMessages() {

    }

    public static final String entityNotFoundExceptionMessage =  "The server has not found anything matching the Request-URI or the resource is not available.";
    public static final String entitySaveExceptionMessage = "An unexpected error occurred on the server.";
    public static final String credentialsValidationExceptionMessageForMail = "Please provide a valid email address";
    public static final String credentialsValidationExceptionMessageForPassword = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and be between 8 and 20 characters long";


}
