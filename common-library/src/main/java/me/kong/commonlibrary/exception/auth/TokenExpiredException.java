package me.kong.commonlibrary.exception.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
