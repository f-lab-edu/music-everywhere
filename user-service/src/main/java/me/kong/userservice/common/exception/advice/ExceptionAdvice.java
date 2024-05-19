package me.kong.userservice.common.exception.advice;


import me.kong.userservice.common.exception.DuplicateElementException;
import me.kong.userservice.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorInfo> noSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ResponseEntity<ErrorInfo> DuplicateElementException(DuplicateElementException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.CONFLICT);
    }
}
