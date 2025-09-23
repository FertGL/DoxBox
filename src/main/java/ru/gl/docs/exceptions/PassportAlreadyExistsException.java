package ru.gl.docs.exceptions;

public class PassportAlreadyExistsException extends RuntimeException{

    public PassportAlreadyExistsException(String message) {
        super(message);
    }
}
