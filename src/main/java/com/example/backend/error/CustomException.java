package com.example.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class CustomException extends Throwable {
    private final ErrorCode errorCode;

}
