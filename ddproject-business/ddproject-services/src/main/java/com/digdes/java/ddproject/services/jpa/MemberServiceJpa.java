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
        Optional<Member> memberOptional = memberRepository.findById(id);
//        Или стоит бросать исключение?
        if (memberOptional.isEmpty()) {
            return new MemberDto();
        }
        return memberMapper.toMemberDto(memberOptional.get());
    }

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = memberMapper.fromMemberDto(memberDto);
//        Надо ли это делать?
        member.setId(null);
        member.setStatus(MemberStatus.ACTIVE);
        Member createdMember = memberRepository.save(member);
        return memberMapper.toMemberDto(createdMember);
    }

    @Override
    public MemberDto update(MemberDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new NullIdException("Id can't be null");
        }
        Member member = memberMapper.fromMemberDto(dto);
        Optional<Member> memberOptional = memberRepository.findById(member.getId());
        if(memberOptional.isEmpty()){
            throw new NoSuchElementException(String.format("Member with id = %d not exists", member.getId()));
        }
        return memberMapper.toMemberDto(memberRepository.save(member));
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
