package edu.school21.restful.controllers;

import edu.school21.restful.exceptions.UserNotFound;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NumberFormatException.class, UserNotFound.class})
    protected ResponseEntity<Object> handle(RuntimeException ex, WebRequest request) {
        if (ex instanceof UserNotFound) {
            return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
        }
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
