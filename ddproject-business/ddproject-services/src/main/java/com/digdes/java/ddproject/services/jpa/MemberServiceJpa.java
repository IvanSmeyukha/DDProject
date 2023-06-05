package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.common.exceptions.NullIdException;
import com.digdes.java.ddproject.dto.filters.SearchMemberFilter;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.MemberSpecification;
import com.digdes.java.ddproject.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceJpa implements MemberService {
    private final MemberRepositoryJpa memberRepository;
    private final MemberMapper memberMapper;


    @Override
    public MemberDto findById(Long id) {
        Member member = memberRepository.findById(id).orElse(new Member());
        return memberMapper.toMemberDto(member);
    }

    public MemberDto findByAccountUsername(String username) {
        Member member = memberRepository.findMemberByAccount_Username(username).orElse(new Member());
        return memberMapper.toMemberDto(member);
    }

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = memberMapper.fromMemberDto(memberDto);
        member.setStatus(MemberStatus.ACTIVE);
        return memberMapper.toMemberDto(memberRepository.save(member));
    }

    @Transactional
    @Override
    public MemberDto update(Long id, MemberDto dto) {
        Member member = memberMapper.fromMemberDto(dto);
        member.setId(id);
        Member updatedMember = memberRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("Member with id = %d not exists", id))
        );
        return memberMapper.toMemberDto(memberRepository.save(updatedMember));
    }

    @Transactional
    @Override
    public MemberDto delete(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isEmpty()) {
            return new MemberDto();
        }
        Member member = memberOptional.get();
        member.setStatus(MemberStatus.DELETED);
        memberRepository.save(member);
        return memberMapper.toMemberDto(member);
    }

    @Override
    public List<MemberDto> search(SearchMemberFilter filter) {
        List<Member> members = memberRepository.findAll(MemberSpecification.getSpec(filter));
        return members.stream().map(memberMapper::toMemberDto).toList();
    }
}
