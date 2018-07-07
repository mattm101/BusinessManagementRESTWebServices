package com.springproject27.springproject.user;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email){
        super("Email " + email + " already exists");
    }
}
