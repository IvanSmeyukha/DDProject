package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepositoryJpa extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findMemberByAccount_Username(String username);

    @Query(value = """
            select m
            from Member m
            where m.id = :id and m.status <> 'DELETED'
            """)
    Optional<Member> findMemberById(@Param("id") Long id);

    Optional<Member> findMemberByAccount_Id(Long account);

    Optional<Member> findMemberByIdNotAndAccount_Id(Long memberId, Long accountId);
}
