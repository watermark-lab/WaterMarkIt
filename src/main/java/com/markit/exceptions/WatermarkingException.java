package com.markit.exceptions;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class WatermarkingException extends RuntimeException {
    public WatermarkingException(String message, Throwable cause) {
        super(message, cause);
    }

    public WatermarkingException(String message) {
        super(message);
    }
}
