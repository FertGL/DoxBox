package ru.gl.docs.exceptions;

public class PassportNotFoundException extends RuntimeException {

    public PassportNotFoundException(String message) {
        super(message);
    }
}
