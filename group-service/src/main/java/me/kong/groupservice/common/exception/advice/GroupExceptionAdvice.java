package me.kong.groupservice.common.exception.advice;


import lombok.RequiredArgsConstructor;
import me.kong.groupservice.common.exception.DuplicateElementException;
import me.kong.groupservice.common.exception.ErrorInfo;
import me.kong.groupservice.common.exception.ForbiddenAccessException;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class GroupExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorInfo> noSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ResponseEntity<ErrorInfo> DuplicateElementException(DuplicateElementException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorInfo> ForbiddenAccessException(ForbiddenAccessException e) {
        return new ResponseEntity<>(new ErrorInfo(e), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoLoggedInProfileException.class)
    public ResponseEntity<ErrorInfo> NoLoggedInProfileException(NoLoggedInProfileException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getClass().getSimpleName(), "가입하지 않은 그룹입니다."), HttpStatus.FORBIDDEN);
    }

}
