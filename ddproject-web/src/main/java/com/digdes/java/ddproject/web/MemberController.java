package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.dto.filters.SearchMemberFilter;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public MemberDto getById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping("/member")
    public MemberDto create(@RequestBody @Valid MemberDto dto){
        return memberService.create(dto);
    }

    @PutMapping("/member")
    public MemberDto update(@RequestBody @Valid MemberDto dto){
        return memberService.update(dto);
    }

    @DeleteMapping("/member/{id}")
    public MemberDto delete(@PathVariable Long id){
        return memberService.delete(id);
    }

    @GetMapping("/member")
    public List<MemberDto> search(@RequestBody SearchMemberFilter filter) {
        return memberService.search(filter);
    }

}
