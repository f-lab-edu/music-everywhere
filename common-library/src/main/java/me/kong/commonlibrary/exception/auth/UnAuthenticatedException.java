package me.kong.commonlibrary.exception.auth;

public class UnAuthenticatedException extends RuntimeException {
    public UnAuthenticatedException() {
        super();
    }

    public UnAuthenticatedException(String message) {
        super(message);
    }
}
