package me.kong.groupservice.common.exception;


public class GroupFullException extends RuntimeException {
    public GroupFullException() {
        super();
    }

    public GroupFullException(String message) {
        super(message);
    }
}
