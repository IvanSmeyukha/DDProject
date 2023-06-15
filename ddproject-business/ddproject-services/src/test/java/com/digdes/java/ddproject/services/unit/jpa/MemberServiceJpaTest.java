package com.digdes.java.ddproject.services.unit.jpa;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.common.exceptions.NotUniqueAccountException;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.security.UserAccountDto;
import com.digdes.java.ddproject.mapping.filters.SearchMemberFilterMapper;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.mapping.useraccount.UserAccountMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.services.jpa.MemberServiceJpa;
import com.digdes.java.ddproject.services.jpa.UserAccountServiceJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceJpaTest {
    @Spy
    private MemberRepositoryJpa memberRepository;
    @Spy
    private MemberMapper memberMapper;
    @Spy
    private SearchMemberFilterMapper filterMapper;
    @Mock
    private UserAccountServiceJpa userAccountService;
    @InjectMocks
    private MemberServiceJpa memberService;

    private MemberDto createMemberDto() {
        return MemberDto.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .account(1L)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    private Long getRandomLong() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }


    @Test
    void findById_MemberWithSuchIdNotExist_ReturnEmptyMemberDto() {
        when(memberRepository.findMemberById(any())).thenReturn(Optional.empty());

        Assertions.assertNull(memberService.findById(1L).getFirstName());
    }

    @Test
    void findById_MemberWithSuchIdExist_ReturnMemberDto() {
        Member member = createMember();

        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(member));

        Assertions.assertEquals(member.getFirstName(), memberService.findById(1L).getFirstName());
    }

    @Test
    void findByAccountUsername_MemberWithSuchUsernameExist_ReturnMemberDto() {
        Member member = createMember();
        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(member));

        MemberDto memberDto = memberService.findById(1L);

        Assertions.assertEquals(member.getFirstName(), memberDto.getFirstName());
    }

    @Test
    void findByAccountUsername_MemberWithSuchUsernameNotExist_ReturnMemberDto() {
        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(new Member()));

        MemberDto memberDto = memberService.findById(2L);

        Assertions.assertNull(memberDto.getFirstName());
    }


    @Test
    void create_MemberWithSuchAccountNotExist_MemberCreated() {
        MemberDto dto = createMemberDto();
        Member member = memberMapper.fromMemberDto(dto);
        Long id = getRandomLong();
        member.setId(id);
        UserAccountDto user = new UserAccountDto();
        user.setId(getRandomLong());
        when(userAccountService.findById(any())).thenReturn(user);
        when(memberRepository.save(any())).thenReturn(member);

        MemberDto createdMember = memberService.create(dto);

        Assertions.assertNotNull(createdMember);
        Assertions.assertEquals(dto.getFirstName(), createdMember.getFirstName());
    }

    @Test
    void create_MemberWithSuchAccountAlreadyExist_Exception() {
        MemberDto dto = createMemberDto();
        Member member = memberMapper.fromMemberDto(dto);
        Long id = getRandomLong();
        member.setId(id);
        UserAccountDto user = new UserAccountDto();
        user.setId(getRandomLong());
        when(userAccountService.findById(any())).thenReturn(user);
        when(memberRepository.findMemberByAccount_Id(any())).thenReturn(Optional.of(member));

        Assertions.assertThrows(NotUniqueAccountException.class, () -> memberService.create(dto));
    }

    @Test
    void update_MemberWithSuchIdNotExist_Exception() {
        MemberDto dto = createMemberDto();
        Long id = getRandomLong();
        UserAccountDto user = new UserAccountDto();
        user.setId(getRandomLong());
        when(memberRepository.findMemberById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> memberService.update(id, dto));
    }

    @Test
    void update_MemberWithSuchIdExist_MemberUpdated() {
        Long id = getRandomLong();
        Member originalMember = createMember();
        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(originalMember));
        Member newMember = createMember();
        when(memberRepository.save(any())).thenReturn(newMember);

        MemberDto updatedMember = memberService.update(id, memberMapper.toMemberDto(newMember));

        Assertions.assertEquals(newMember.getFirstName(), updatedMember.getFirstName());
    }

    @Test
    void update_MemberWithSuchAccountAlreadyExist_Exception() {
        Long id = getRandomLong();
        UserAccountDto user = new UserAccountDto();
        user.setId(getRandomLong());
        when(userAccountService.findById(any())).thenReturn(user);
        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(new Member()));

        when(memberRepository.findMemberByIdNotAndAccount_Id(any(), any())).thenReturn(Optional.of(createMember()));

        Assertions.assertThrows(NotUniqueAccountException.class, () -> memberService.update(id, createMemberDto()));
    }

    @Test
    void update_MemberWithSuchAccountIsUpdatableMember_MemberUpdated() {
        Member originalMember = createMember();
        Long id = getRandomLong();
        when(memberRepository.findMemberByIdNotAndAccount_Id(any(), any())).thenReturn(Optional.empty());
        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(originalMember));
        Member newMember = createMember();
        when(memberRepository.save(any())).thenReturn(newMember);

        MemberDto updatedMember = memberService.update(id, memberMapper.toMemberDto(newMember));
        Assertions.assertEquals(newMember.getFirstName(), updatedMember.getFirstName());
        Assertions.assertNotEquals(originalMember.getFirstName(), updatedMember.getFirstName());
    }

    @Test
    void delete_MemberWithSuchIdNotExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();
        when(memberRepository.findMemberById(any())).thenReturn(Optional.empty());

        MemberDto deletedMember = memberService.delete(id);

        Assertions.assertNull(deletedMember.getFirstName());
    }


    @Test
    void isMemberExist_MemberWithSuchIdNotExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();
        when(memberRepository.findMemberById(any())).thenReturn(Optional.empty());

        Assertions.assertFalse(memberService.isMemberExist(id));
    }

    @Test
    void isMemberExist_MemberWithSuchIdExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();
        when(memberRepository.findMemberById(any())).thenReturn(Optional.of(createMember()));

        Assertions.assertTrue(memberService.isMemberExist(id));
    }


}
