package com.example.STUDENT.exception;

public class DuplicateRegisterNumberException extends RuntimeException {

    public DuplicateRegisterNumberException() {
        super("Register number already exists");
    }
}
