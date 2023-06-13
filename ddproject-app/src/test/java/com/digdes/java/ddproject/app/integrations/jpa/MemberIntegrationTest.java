package com.digdes.java.ddproject.app.integrations.jpa;

import com.digdes.java.ddproject.app.MainApp;
import com.digdes.java.ddproject.app.integrations.IntegrationEnvironment;
import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.common.exceptions.NotUniqueAccountException;
import com.digdes.java.ddproject.dto.filters.SearchMemberFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.UserAccount;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.services.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest(classes = MainApp.class)
class MemberIntegrationTest extends IntegrationEnvironment {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepositoryJpa memberRepository;

    @Test
    void findById_MemberWithSuchIdNotExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();

        MemberDto member = memberService.findById(id);

        Assertions.assertNotNull(member);
        Assertions.assertNull(member.getFirstName());
    }

    @Test
    void findById_MemberWithSuchIdExist_ReturnMemberDto() {
        Member member = createMember();
        Long id = memberRepository.save(member).getId();

        MemberDto foundMember = memberService.findById(id);

        Assertions.assertNotNull(foundMember);
        Assertions.assertEquals(member.getFirstName(), foundMember.getFirstName());
    }

    @Test
    void findByAccountUsername_MemberWithSuchUsernameExist_ReturnMemberDto() {
        Member member = createMember();
        member.setAccount(UserAccount.builder().id(1L).build());
        memberRepository.save(member);

        MemberDto foundMember = memberService.findByAccountUsername("root");

        Assertions.assertNotNull(foundMember);
        Assertions.assertEquals(member.getFirstName(), foundMember.getFirstName());
    }

    @Test
    void findByAccountUsername_MemberWithSuchUsernameNotExist_ReturnEmptyMemberDto() {
        MemberDto foundMember = memberService.findByAccountUsername(UUID.randomUUID().toString());

        Assertions.assertNotNull(foundMember);
        Assertions.assertNull(foundMember.getFirstName());
    }

    @Test
    void create_AccountNotGiven_MemberCreated() {
        MemberDto member = createMemberDto();

        MemberDto createdMember = memberService.create(member);

        Member foundMember = memberRepository.findById(createdMember.getId()).get();
        Assertions.assertEquals(member.getFirstName(), foundMember.getFirstName());
    }

    @Test
    void create_MemberWithSuchAccountNotExist_MemberCreated() {
        MemberDto member = createMemberDto();
        member.setAccount(1L);

        MemberDto createdMember = memberService.create(member);

        Member foundMember = memberRepository.findById(createdMember.getId()).get();
        Assertions.assertEquals(member.getFirstName(), foundMember.getFirstName());
    }

    @Test
    void create_AccountWithSuchIdNotExist_Exception() {
        MemberDto memberForSave = createMemberDto();
        memberForSave.setAccount(getRandomLong());

        Assertions.assertThrows(NoSuchElementException.class, () -> memberService.create(memberForSave));
    }

    @Test
    void create_MemberWithSuchAccountAlreadyExist_Exception() {
        Member member = createMember();
        member.setAccount(UserAccount.builder().id(1L).build());
        memberRepository.save(member);

        MemberDto memberForSave = createMemberDto();
        memberForSave.setAccount(1L);

        Assertions.assertThrows(NotUniqueAccountException.class, () -> memberService.create(memberForSave));
    }

    @Test
    void update_MemberWithSuchIdNotExist_Exception() {
        MemberDto dto = createMemberDto();
        Long id = getRandomLong();

        Assertions.assertThrows(NoSuchElementException.class, () -> memberService.update(id, dto));
    }

    @Test
    void update_AccountWithSuchIdNotExist_Exception() {
        MemberDto dto = createMemberDto();
        dto.setAccount(getRandomLong());
        Long id = getRandomLong();

        Assertions.assertThrows(NoSuchElementException.class, () -> memberService.update(id, dto));
    }

    @Test
    void update_MemberWithSuchIdExist_MemberUpdated() {
        Long id = memberRepository.save(createMember()).getId();
        MemberDto dto = createMemberDto();

        memberService.update(id, dto);

        Member member = memberRepository.findMemberById(id).get();
        Assertions.assertEquals(dto.getFirstName(), member.getFirstName());
    }

    @Test
    void update_MemberWithSuchAccountAlreadyExist_Exception() {
        Member originalMember = createMember();
        Long id = memberRepository.save(originalMember).getId();
        Member otherMember = createMember();
        otherMember.setAccount(UserAccount.builder().id(1L).build());
        memberRepository.save(otherMember);
        MemberDto updateDto = createMemberDto();
        updateDto.setAccount(1L);

        Assertions.assertThrows(NotUniqueAccountException.class, () -> memberService.update(id, updateDto));
    }

    @Test
    void update_MemberWithSuchAccountIsUpdatableMember_MemberUpdated() {
        Member originalMember = createMember();
        Long id = memberRepository.save(originalMember).getId();
        MemberDto updateDto = createMemberDto();
        updateDto.setAccount(1L);

        MemberDto updatedMember = memberService.update(id, updateDto);
        Assertions.assertEquals(updateDto.getFirstName(), updatedMember.getFirstName());
        Assertions.assertNotEquals(originalMember.getFirstName(), updatedMember.getFirstName());
    }

    @Test
    void delete_MemberWithSuchIdExist_SetMemberStatusToDeleted() {
        Member originalMember = createMember();
        Long id = memberRepository.save(originalMember).getId();

        memberService.delete(id);

        Member deletedMember = memberRepository.findById(id).get();
        Assertions.assertEquals(originalMember.getFirstName(), deletedMember.getFirstName());
        Assertions.assertEquals(MemberStatus.DELETED, deletedMember.getStatus());
    }

    @Test
    void delete_MemberWithSuchIdNotExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();

        MemberDto deletedMember = memberService.delete(id);

        Assertions.assertNull(deletedMember.getFirstName());
    }

    @Test
    void search_SearchByName_ReturnMemberList() {
        Member targetMember = createMember();
        targetMember.setFirstName("АлЕкСаНдР");
        memberRepository.save(targetMember);
        String searchName = "ЛЕКС";
        SearchMemberFilterDto filter = new SearchMemberFilterDto();
        filter.setFirstName(searchName);
        List<MemberDto> members = memberService.search(filter);

        Assertions.assertEquals(1, members.size());
        Assertions.assertEquals(members.get(0).getFirstName(), targetMember.getFirstName());
    }

    @Test
    void isMemberExist_MemberWithSuchIdNotExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();

        Assertions.assertFalse(memberService.isMemberExist(id));
    }

    @Test
    void isMemberExist_MemberWithSuchIdExist_ReturnEmptyMemberDto() {
        Long id = memberRepository.save(createMember()).getId();

        Assertions.assertTrue(memberService.isMemberExist(id));
    }
}
