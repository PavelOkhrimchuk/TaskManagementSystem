package org.ohrim.taskmanagementsystem.exception.user;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
