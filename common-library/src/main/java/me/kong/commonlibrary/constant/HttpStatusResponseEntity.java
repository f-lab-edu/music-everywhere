package me.kong.commonlibrary.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpStatusResponseEntity {
    public static final ResponseEntity<HttpStatus> RESPONSE_OK = ResponseEntity.status(HttpStatus.OK).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_UNAUTHENTICATED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_UNAUTHORIZED = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}