package me.kong.groupservice.common.exception;

public class DuplicateElementException extends RuntimeException {
    public DuplicateElementException() {
        super();
    }

    public DuplicateElementException(String message) {
        super(message);
    }
}
