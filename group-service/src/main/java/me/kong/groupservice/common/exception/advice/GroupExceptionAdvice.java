package me.kong.groupservice.common.exception.advice;


import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.exception.ErrorInfo;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
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

    @ExceptionHandler(NoLoggedInProfileException.class)
    public ResponseEntity<ErrorInfo> NoLoggedInProfileException(NoLoggedInProfileException e) {
        return new ResponseEntity<>(new ErrorInfo(e.getClass().getSimpleName(), "가입하지 않은 그룹입니다."), HttpStatus.FORBIDDEN);
    }

}
