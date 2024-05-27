package me.kong.commonlibrary.exception.common;

public class DuplicateElementException extends RuntimeException {
    public DuplicateElementException() {
        super();
    }

    public DuplicateElementException(String message) {
        super(message);
    }
}
