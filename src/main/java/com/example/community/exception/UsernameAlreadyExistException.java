package com.example.community.exception;

public class UsernameAlreadyExistException extends RuntimeException{
    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
