package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepositoryJpa extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
}
