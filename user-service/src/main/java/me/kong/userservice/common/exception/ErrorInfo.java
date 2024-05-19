package me.kong.userservice.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorInfo {
    private String errorType;
    private String msg;

    public ErrorInfo(String errorType, String msg) {
        this.errorType = errorType;
        this.msg = msg;
    }

    public ErrorInfo(Throwable e) {
        this.errorType = e.getClass().getSimpleName();
        this.msg = e.getMessage();
    }
}
