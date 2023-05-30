package com.digdes.java.ddproject.dto.error;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
    String description;
    HttpStatus status;
    String exceptionName;
    String exceptionMessage;
    List<String> stacktrace;
}
