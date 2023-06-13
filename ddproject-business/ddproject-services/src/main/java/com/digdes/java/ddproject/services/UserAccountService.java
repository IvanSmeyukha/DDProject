package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.dto.security.UserAccountDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService extends UserDetailsService {
    UserAccountDto save(UserAccountDto dto);
}
