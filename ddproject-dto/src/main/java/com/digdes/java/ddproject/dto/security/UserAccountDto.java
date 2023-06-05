package com.digdes.java.ddproject.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Account's username")
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Account's password")
    private String password;
}
