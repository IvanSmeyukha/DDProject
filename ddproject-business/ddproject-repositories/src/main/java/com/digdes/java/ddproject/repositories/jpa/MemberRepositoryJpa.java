package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface MemberRepositoryJpa extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

}
