package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@NoArgsConstructor
@Getter
public class SuccessDetails {

    private boolean success;
    private String message;
    private Object data;
    private static HttpStatus httpStatus;

    public SuccessDetails(String message, Object data, HttpStatus httpStatus) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    public SuccessDetails(String message, Object data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<SuccessDetails> success(String message, Object data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new SuccessDetails(message, data, httpStatus), httpStatus);
    }

    public static ResponseEntity<SuccessDetails> success(String message, Object data) {
        return new ResponseEntity<>(new SuccessDetails(message, data), HttpStatus.OK);
    }
}


































    /*public static ResponseEntity<?> success(String message, Object data,HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("message", message);
        map.put("data", data);
        //map.put("status", httpStatus.value());
        return new ResponseEntity<>(map, httpStatus);
    }*/