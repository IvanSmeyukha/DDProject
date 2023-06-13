package com.digdes.java.ddproject.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Account's info")
public class UserAccountDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Account's id")
    private Long id;
    @NotBlank(message = "Username is required field")
    @Schema(description = "Account's username")
    private String username;
    @NotBlank(message = "Password is required field")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Account's password")
    private String password;
}
