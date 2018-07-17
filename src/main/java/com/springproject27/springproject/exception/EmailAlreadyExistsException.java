package com.springproject27.springproject.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email){
        super("Email " + email + " already exists");
    }
}
