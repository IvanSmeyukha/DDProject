package com.digdes.java.ddproject.repositories;

import com.digdes.java.ddproject.common.exceptions.NotFoundException;
import com.digdes.java.ddproject.model.Member;

import java.util.List;

public interface MemberRepository {

    Member create(Member member);

    Member update(Member member);

    Member getById(Long id) throws NotFoundException;

    List<Member> getAll();

    Member deleteById(Long id) throws NotFoundException;
}
