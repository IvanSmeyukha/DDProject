package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.dto.error.ApiErrorResponse;
import com.digdes.java.ddproject.dto.security.UserAccountDto;
import com.digdes.java.ddproject.services.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @Operation(summary = "Register new account",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping(value = "/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserAccountDto signup(@Parameter(name = "Account info") @RequestBody @Valid UserAccountDto dto) {
        return userAccountService.save(dto);
    }

}
