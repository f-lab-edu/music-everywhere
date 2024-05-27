package me.kong.commonlibrary.exception.advice;


import me.kong.commonlibrary.exception.ErrorInfo;
import me.kong.commonlibrary.exception.auth.InvalidTokenException;
import me.kong.commonlibrary.exception.auth.TokenExpiredException;
import me.kong.commonlibrary.exception.auth.UnAuthenticatedException;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static me.kong.commonlibrary.constant.HttpStatusResponseEntity.*;

@RestControllerAdvice
public class AuthExceptionAdvice {

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<HttpStatus> unAuthenticatedException() {
        return RESPONSE_UNAUTHENTICATED;
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<HttpStatus> unAuthorizedException() {
        return RESPONSE_UNAUTHORIZED;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorInfo> tokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorInfo> invalidTokenException(InvalidTokenException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.FORBIDDEN);
    }

}
