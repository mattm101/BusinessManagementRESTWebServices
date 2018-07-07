package com.springproject27.springproject.user;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice{

    @ExceptionHandler(UserNotFoundException.class)
    void userNotFoundHandler(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    void emailExistsHandler(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

}
