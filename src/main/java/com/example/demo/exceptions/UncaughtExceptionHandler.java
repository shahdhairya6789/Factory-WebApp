package com.example.demo.exceptions;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.models.CommonResponse;

@ControllerAdvice
@RestController
public class UncaughtExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UncaughtExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
        LOGGER.error("Internal server Error:{}", ex.getMessage(), ex);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        if (StringUtils.isNotBlank(requestURI) && requestURI.toLowerCase().contains("/v1")) {
            CommonResponse.ErrorObject errorObject = new CommonResponse.ErrorObject(ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            CommonResponse.ErrorObject errorResp = new CommonResponse.ErrorObject("Server encountered an error !", request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(new CommonResponse<>(errorResp), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        LOGGER.error("UncaughtExceptionHandler.handleIllegalArgumentExceptions Error:{}", ExceptionUtils.getStackTrace(ex), ex);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        if (StringUtils.isNotBlank(requestURI) && requestURI.toLowerCase().contains("/v1")) {
            CommonResponse.ErrorObject errorObject = new CommonResponse.ErrorObject(ex.getMessage(), request.getDescription(false), HttpStatus.UNPROCESSABLE_ENTITY);
            return new ResponseEntity<>(new CommonResponse<>(errorObject), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            String error = "Invalid argument provided!!";
            CommonResponse.ErrorObject errorResp = new CommonResponse.ErrorObject(error, request.getDescription(false), HttpStatus.UNPROCESSABLE_ENTITY);
            return new ResponseEntity<>(new CommonResponse<>(errorResp), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(RuntimeException ex, WebRequest request) {
        LOGGER.error("UncaughtExceptionHandler.handleAccessDeniedException Error:{}", ExceptionUtils.getStackTrace(ex), ex);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        if (StringUtils.isNotBlank(requestURI) && requestURI.toLowerCase().contains("/v1")) {
            CommonResponse.ErrorObject errorObject = new CommonResponse.ErrorObject(ex.getMessage(), request.getDescription(false), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(new CommonResponse<>(errorObject), HttpStatus.FORBIDDEN);
        } else {
            String error = "Requested resource is not accessible!";
            CommonResponse.ErrorObject errorResp = new CommonResponse.ErrorObject(error, request.getDescription(false), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(new CommonResponse<>(errorResp), HttpStatus.FORBIDDEN);
        }
    }
}
