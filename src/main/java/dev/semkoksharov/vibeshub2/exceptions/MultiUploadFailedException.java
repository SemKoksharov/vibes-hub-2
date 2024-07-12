package dev.semkoksharov.vibeshub2.exceptions;

public class MultiUploadFailedException extends RuntimeException {
    public MultiUploadFailedException(String message) {
        super(message);
    }
}
