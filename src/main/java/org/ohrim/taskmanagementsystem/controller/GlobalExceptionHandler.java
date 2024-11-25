package org.ohrim.taskmanagementsystem.controller;

import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.exception.task.InvalidRequestException;
import org.ohrim.taskmanagementsystem.exception.task.ResourceNotFoundException;
import org.ohrim.taskmanagementsystem.exception.task.TaskStatusException;
import org.ohrim.taskmanagementsystem.exception.user.EmailAlreadyTakenException;
import org.ohrim.taskmanagementsystem.exception.user.InvalidCredentialsException;
import org.ohrim.taskmanagementsystem.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<String> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.append(fieldError.getDefaultMessage()).append(" ")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString().trim());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequest(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TaskStatusException.class)
    public ResponseEntity<String> handleTaskStatusException(TaskStatusException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == Status.class) {
            return ResponseEntity.badRequest().body("Invalid status value: " + ex.getValue() +
                    ". Allowed values are: " + String.join(", ", Arrays.toString(Status.values())));
        }
        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getMessage());
    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }



}
