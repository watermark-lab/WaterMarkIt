package com.markit.exceptions;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class UnsupportedFileTypeException extends RuntimeException {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}