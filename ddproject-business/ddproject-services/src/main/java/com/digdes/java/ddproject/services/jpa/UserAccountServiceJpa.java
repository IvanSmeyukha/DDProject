package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.dto.security.UserAccountDto;
import com.digdes.java.ddproject.mapping.useraccount.UserAccountMapper;
import com.digdes.java.ddproject.model.UserAccount;
import com.digdes.java.ddproject.repositories.jpa.UserAccountRepositoryJpa;
import com.digdes.java.ddproject.services.UserAccountService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserAccountServiceJpa implements UserAccountService {
    private final UserAccountRepositoryJpa userAccountRepository;
    private final UserAccountMapper userAccountMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserAccountDto save(UserAccountDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserAccount user = userAccountMapper.fromUserAccountDto(dto);
        return userAccountMapper.toUserAccountDto(userAccountRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        return new User(username, user.getPassword(), Collections.emptyList());
    }

    @PostConstruct
    public void initAdmin() {
        userAccountRepository.findByUsername("root")
                .orElseGet(() -> userAccountRepository.save(UserAccount.builder()
                        .username("root")
                        .password(passwordEncoder.encode("root"))
                        .build()));
    }
}
