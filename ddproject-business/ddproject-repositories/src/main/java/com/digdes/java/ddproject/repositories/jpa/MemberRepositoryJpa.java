package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface MemberRepositoryJpa extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findMemberByAccount_Username(String username);
}
