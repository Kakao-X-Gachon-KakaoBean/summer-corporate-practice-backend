package com.kakaobean.common;

import com.kakaobean.common.dto.ApplicationExceptionResponse;
import com.kakaobean.core.common.ApplicationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.security.sasl.AuthenticationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    private static final String INTERNAL_SERVER_ERROR_CODE = "서버 오류입니다.";
    private static final String DATABASE_SERVER_ERROR_CODE = "데이터베이스 오류입니다.";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationExceptionResponse> handleApplicationException(ApplicationException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), BAD_REQUEST, e.getMessage());
        ApplicationExceptionResponse exceptionResponse = new ApplicationExceptionResponse(e.getMessage(), e.getErrorCode(), e.getStatus());
        return ResponseEntity.status(HttpStatus.valueOf(exceptionResponse.getStatus())).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApplicationExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ApplicationExceptionResponse exceptionResponse = new ApplicationExceptionResponse(e.getValue().toString(), "G005", 400);
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), BAD_REQUEST, "@Valid");
        return ResponseEntity.status(BAD_REQUEST.value()).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getFieldError().getDefaultMessage();
        ApplicationExceptionResponse exceptionResponse = new ApplicationExceptionResponse(message, "G004", 400);
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), BAD_REQUEST, "@Valid");
        return ResponseEntity.status(BAD_REQUEST.value()).body(exceptionResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ApplicationExceptionResponse> handleAuthenticationException(AuthenticationException e) {
        ApplicationExceptionResponse exceptionResponse = new ApplicationExceptionResponse(e.getMessage() , "G003", 400);
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(BAD_REQUEST.value()).body(exceptionResponse);

    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApplicationExceptionResponse> handleDataAccessException(DataAccessException e) {
        ApplicationExceptionResponse exceptionResponse = new ApplicationExceptionResponse(DATABASE_SERVER_ERROR_CODE, "G002", 500);
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationExceptionResponse> handleRuntimeException(RuntimeException e) {
        ApplicationExceptionResponse exceptionResponse = new ApplicationExceptionResponse(INTERNAL_SERVER_ERROR_CODE, "G001", 500);
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
