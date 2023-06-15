package com.digdes.java.ddproject.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Error info")
public class ApiErrorResponse {
    @Schema(description = "Error description")
    String description;
    @Schema(description = "Error status")
    HttpStatus status;
    @Schema(description = "Exception name")
    String exceptionName;
    @Schema(description = "Exception message")
    String exceptionMessage;
    @Schema(description = "Stacktrace")
    List<String> stacktrace;
}
