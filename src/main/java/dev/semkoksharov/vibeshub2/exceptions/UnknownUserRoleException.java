package dev.semkoksharov.vibeshub2.exceptions;

public class UnknownUserRoleException extends RuntimeException{

    public UnknownUserRoleException(String message) {
        super(message);
    }
}
