package com.dreamtracker.app.infrastructure.exception;


import lombok.Builder;
import lombok.Data;


@Builder
public record ErrorObject(Integer code,String message){

}
