package com.springproject27.springproject.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id){
        super("User " + id + " not found");
    }
}
