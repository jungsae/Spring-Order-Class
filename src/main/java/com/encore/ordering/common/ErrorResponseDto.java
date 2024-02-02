package com.encore.ordering.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto
{
    public static ResponseEntity<Map<String, Object>> makeMessage(HttpStatus status, String message){
        Map<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(status.value()));
        body.put("status message", status.getReasonPhrase());
        body.put("error message", message);
        return new ResponseEntity<>(body, status);
    }
}
