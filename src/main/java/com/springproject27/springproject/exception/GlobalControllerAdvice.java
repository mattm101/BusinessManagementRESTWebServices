package com.springproject27.springproject.exception;

import com.springproject27.springproject.exception.EmailAlreadyExistsException;
import com.springproject27.springproject.exception.EntityNotFoundException;
import com.springproject27.springproject.user.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice(assignableTypes = UserController.class)
public class GlobalControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    void userNotFoundHandler(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    void emailExistsHandler(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }



}
