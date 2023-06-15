package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.common.exceptions.NotUniqueAccountException;
import com.digdes.java.ddproject.dto.filters.SearchMemberFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.mapping.filters.SearchMemberFilterMapper;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.repositories.filters.SearchMemberFilter;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.MemberSpecification;
import com.digdes.java.ddproject.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceJpa implements MemberService {
    private final MemberRepositoryJpa memberRepository;
    private final MemberMapper memberMapper;
    private final UserAccountServiceJpa userAccountService;
    private final SearchMemberFilterMapper filterMapper;


    @Override
    public MemberDto findById(Long id) {
        Member member = memberRepository.findMemberById(id).orElse(new Member());
        return memberMapper.toMemberDto(member);
    }

    @Override
    public MemberDto findByAccountUsername(String username) {
        Member member = memberRepository.findMemberByAccount_Username(username).orElse(new Member());
        return memberMapper.toMemberDto(member);
    }

    @Transactional
    @Override
    public MemberDto create(MemberDto dto) {
        if (!ObjectUtils.isEmpty(dto.getAccount())) {
            if(ObjectUtils.isEmpty(userAccountService.findById(dto.getAccount()).getId())){
                throw new NoSuchElementException(String.format("Account with id = %d not exist", dto.getAccount()));
            }
            Optional<Member> memberWithSuchAccount = memberRepository.findMemberByAccount_Id(dto.getAccount());
            if (memberWithSuchAccount.isPresent()) {
                throw new NotUniqueAccountException(
                        String.format("Account with id = %d already attached with Member with id = %d",
                                dto.getId(),
                                memberWithSuchAccount.get().getId()
                        )
                );
            }
        }
        Member member = memberMapper.fromMemberDto(dto);
        member.setStatus(MemberStatus.ACTIVE);
        Member createdMember = memberRepository.save(member);
        log.info("Create member with id = {}", createdMember.getId());
        return memberMapper.toMemberDto(createdMember);
    }

    @Transactional
    @Override
    public MemberDto update(Long id, MemberDto dto) {
        if (!ObjectUtils.isEmpty(dto.getAccount()) && ObjectUtils.isEmpty(userAccountService.findById(dto.getAccount()).getId())) {
            throw new NoSuchElementException(String.format("Account with id = %d not exist", dto.getAccount()));
        }
        Optional<Member> memberWithSuchAccount = memberRepository.findMemberByIdNotAndAccount_Id(id, dto.getAccount());
        if (memberWithSuchAccount.isPresent()) {
            throw new NotUniqueAccountException(
                    String.format("Account with id = %d already attached with Member with id = %d",
                            dto.getId(),
                            memberWithSuchAccount.get().getId()
                    )
            );
        }
        memberRepository.findMemberById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("Member with id = %d not exists", id))
        );
        Member member = memberMapper.fromMemberDto(dto);
        member.setId(id);
        Member updatedMember = memberRepository.save(member);
        log.info("Update member with id = {}", updatedMember.getId());
        return memberMapper.toMemberDto(updatedMember);
    }

    @Transactional
    @Override
    public MemberDto delete(Long id) {
        Optional<Member> memberOptional = memberRepository.findMemberById(id);
        if (memberOptional.isEmpty()) {
            return new MemberDto();
        }
        Member member = memberOptional.get();
        member.setStatus(MemberStatus.DELETED);
        memberRepository.save(member);
        Member deletedMember = memberRepository.save(member);
        log.info("Delete member with id = {}", deletedMember.getId());
        return memberMapper.toMemberDto(deletedMember);
    }

    @Override
    public List<MemberDto> search(SearchMemberFilterDto filter) {
        List<Member> members = memberRepository.findAll(MemberSpecification.getSpec(filterMapper.fromDto(filter)));
        return members.stream().map(memberMapper::toMemberDto).toList();
    }

    @Override
    public boolean isMemberExist(Long id) {
        return memberRepository.findMemberById(id).isPresent();
    }
}
