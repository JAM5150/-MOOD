package com.example.backend.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = {NotFoundDataException.class})
    public ResponseEntity<Object> handleNotFoundDataException(NotFoundDataException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiErrorMessage apiException = new ApiErrorMessage(
                "ExceptionDetailMessage.DATA_NOT_FOUND_MESSAGE",
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        HttpStatus httpStatus = e.getErrorCode().getHttpStatus();

        logger.error(String.valueOf(e.getErrorCode().getDetail()));
        ApiErrorMessage apiException = new ApiErrorMessage(
                e.getErrorCode().getDetail(),
                e.getErrorCode().getHttpStatus(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }
}