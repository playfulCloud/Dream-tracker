package com.dreamtracker.app.exception;


import lombok.Builder;
import lombok.Data;


@Builder
public record ErrorObject(Integer code,String message){

}
