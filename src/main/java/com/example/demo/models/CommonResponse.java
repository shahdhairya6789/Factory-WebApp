package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private T data;
    private String message;
    private List<ErrorObject> errorObject;
    public CommonResponse(String message){
        this.message = message;
    }

    public CommonResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public CommonResponse(ErrorObject errorObject) {
        this.errorObject = Collections.singletonList(errorObject);
    }

    public CommonResponse(List<ErrorObject> errorObject, String message) {
        this.errorObject = errorObject;
        this.message = message;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorObject {
        private String error;
        private String reason;
        private String field;
    }
}
