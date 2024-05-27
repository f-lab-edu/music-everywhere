package me.kong.commonlibrary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    private String errorType;
    private String msg;

    public ErrorInfo(Throwable e) {
        this.errorType = e.getClass().getSimpleName();
        this.msg = e.getMessage();
    }
}
