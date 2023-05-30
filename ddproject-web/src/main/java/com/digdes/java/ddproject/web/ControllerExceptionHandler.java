package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.common.exceptions.MemberNotInProjectException;
import com.digdes.java.ddproject.common.exceptions.NullIdException;
import com.digdes.java.ddproject.dto.error.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ApiErrorResponse handleNoSuchElementException(NoSuchElementException exception) {
        return ApiErrorResponse.builder()
                .description("Element not found")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiErrorResponse handleDuplicateKeyException(DuplicateKeyException exception) {
        return ApiErrorResponse.builder()
                .description("Duplicate key")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ApiErrorResponse.builder()
                .description("Argument not valid")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

    @ExceptionHandler(NullIdException.class)
    public ApiErrorResponse handleNullIdException(NullIdException exception) {
        return ApiErrorResponse.builder()
                .description("Id is null")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorResponse handleEntityNotFoundException(EntityNotFoundException exception) {
        return ApiErrorResponse.builder()
                .description("Entity not found")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

    @ExceptionHandler(MemberNotInProjectException.class)
    public ApiErrorResponse handleMemberNotInProjectException(MemberNotInProjectException exception) {
        return ApiErrorResponse.builder()
                .description("Member not involved project")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

//    TODO: Добавить разъяснения к этому исключению
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        return ApiErrorResponse.builder()
                .description("Data Integrity Violation")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ApiErrorResponse.builder()
                .description("Incorrect Enum value")
                .status(HttpStatus.BAD_REQUEST)
                .exceptionName(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .stacktrace(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
                )
                .build();
    }
}
