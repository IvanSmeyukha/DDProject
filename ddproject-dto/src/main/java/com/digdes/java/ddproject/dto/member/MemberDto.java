package com.digdes.java.ddproject.dto.member;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    @NotBlank(message = "First name is required field")
    private String firstName;
    @NotBlank(message = "Last name is required field")
    private String lastName;
    private String patronymic;
    private String position;
    private Long account;
    @Email(message = "Incorrect email format")
    private String email;
    private MemberStatus status;
}
