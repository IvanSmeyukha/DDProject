package com.digdes.java.ddproject.dto.member;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Member info")
public class MemberDto {
    @Schema(description = "Member's id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Schema(description = "Member's first name")
    @NotBlank(message = "First name is required field")
    private String firstName;
    @Schema(description = "Member's last name")
    @NotBlank(message = "Last name is required field")
    private String lastName;
    @Schema(description = "Member's patronymic")
    private String patronymic;
    @Schema(description = "Member's position")
    private String position;
    @Schema(description = "Member's account id")
    private Long account;
    @Schema(description = "Member's email")
    @Email(message = "Incorrect email format")
    private String email;
    @Schema(description = "Member's status")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MemberStatus status;
}
