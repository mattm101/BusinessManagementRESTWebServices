package com.springproject27.springproject.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String email) {
        super("Email " + email + " already exists");
    }
}
