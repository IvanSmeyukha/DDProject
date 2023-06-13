package com.digdes.java.ddproject.dto.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Filter for members searching")
public class SearchMemberFilterDto {
    @Schema(description = "Member's first name")
    private String firstName;
    @Schema(description = "Member's last name")
    private String lastName;
    @Schema(description = "Member's patronymic")
    private String patronymic;
    @Schema(description = "Member's position")
    private String position;
    @Schema(description = "Member's email")
    private String email;
}
