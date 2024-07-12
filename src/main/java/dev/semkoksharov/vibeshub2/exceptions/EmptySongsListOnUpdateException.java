package dev.semkoksharov.vibeshub2.exceptions;

public class EmptySongsListOnUpdateException extends RuntimeException{

    public EmptySongsListOnUpdateException(String message) {
        super(message);
    }
}
