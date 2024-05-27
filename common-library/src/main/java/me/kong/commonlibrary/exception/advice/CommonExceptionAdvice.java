package me.kong.commonlibrary.exception.advice;


import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.commonlibrary.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(DuplicateElementException.class)
    public ResponseEntity<ErrorInfo> duplicateElementException(DuplicateElementException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.CONFLICT);
    }
}
