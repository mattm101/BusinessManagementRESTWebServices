package com.springproject27.springproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice(assignableTypes = {RestController.class})
public class GlobalControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public void entityNotFoundHandler(HttpServletResponse response, EntityNotFoundException e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public void entityExistsHandler(HttpServletResponse response, EntityAlreadyExistsException e) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    void illegalArgumentHandler(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
