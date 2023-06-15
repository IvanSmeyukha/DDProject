package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.dto.security.UserAccountDto;
import com.digdes.java.ddproject.mapping.useraccount.UserAccountMapper;
import com.digdes.java.ddproject.model.UserAccount;
import com.digdes.java.ddproject.repositories.jpa.UserAccountRepositoryJpa;
import com.digdes.java.ddproject.services.UserAccountService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
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
        UserAccount createdAccount = userAccountRepository.save(user);
        log.info("Registered new User with account id = {}", createdAccount.getId());
        return userAccountMapper.toUserAccountDto(createdAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        log.info("User with account id = {} logged in", user.getId());
        return new User(username, user.getPassword(), Collections.emptyList());
    }

    @Override
    public UserAccountDto findById(Long id) {
        return userAccountMapper.toUserAccountDto(userAccountRepository.findById(id).orElse(new UserAccount()));
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
