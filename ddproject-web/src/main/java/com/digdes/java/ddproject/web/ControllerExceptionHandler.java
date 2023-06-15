package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.common.exceptions.ImpossibleDeadlineException;
import com.digdes.java.ddproject.common.exceptions.MemberNotInProjectException;
import com.digdes.java.ddproject.common.exceptions.NotUniqueAccountException;
import com.digdes.java.ddproject.dto.error.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(NoSuchElementException exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorResponse.builder()
                .description("Element not found")
                .status(HttpStatus.NOT_FOUND)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateKeyException(DuplicateKeyException exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorResponse.builder()
                        .description("Duplicate key")
                        .status(HttpStatus.CONFLICT)
                        .exceptionName(exception.getClass().getName())
                        .exceptionMessage(exception.getMessage())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                        )
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiErrorResponse.builder()
                        .description("Argument not valid")
                        .status(HttpStatus.BAD_REQUEST)
                        .exceptionName(exception.getClass().getName())
                        .exceptionMessage(exception.getMessage())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                        )
                        .build());
    }


    @ExceptionHandler(MemberNotInProjectException.class)
    public ResponseEntity<ApiErrorResponse> handleMemberNotInProjectException(MemberNotInProjectException exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiErrorResponse.builder()
                        .description("Member not involved project")
                        .status(HttpStatus.BAD_REQUEST)
                        .exceptionName(exception.getClass().getName())
                        .exceptionMessage(exception.getMessage())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                        )
                        .build());
    }


    @ExceptionHandler(NotUniqueAccountException.class)
    public ResponseEntity<ApiErrorResponse> handleNotUniqueAccountException(NotUniqueAccountException exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorResponse.builder()
                        .description("Not unique account")
                        .status(HttpStatus.CONFLICT)
                        .exceptionName(exception.getClass().getName())
                        .exceptionMessage(exception.getMessage())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                        )
                        .build());
    }

    @ExceptionHandler(ImpossibleDeadlineException.class)
    public ResponseEntity<ApiErrorResponse> handleImpossibleDeadlineException(ImpossibleDeadlineException exception) {
        log.warn("{}: {}", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiErrorResponse.builder()
                        .description("Impossible deadline")
                        .status(HttpStatus.BAD_REQUEST)
                        .exceptionName(exception.getClass().getName())
                        .exceptionMessage(exception.getMessage())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()
                        )
                        .build());
    }
}
