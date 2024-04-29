package com.dreamtracker.app.exception;


import lombok.Data;

@Data
public class ErrorObject {
    private Integer code;
    private String message;
    private String details;
}
