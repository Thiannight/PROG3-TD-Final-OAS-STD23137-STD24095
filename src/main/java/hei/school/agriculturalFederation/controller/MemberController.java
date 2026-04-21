package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.CreateMember;
import hei.school.agriculturalFederation.model.Member;
import hei.school.agriculturalFederation.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Member> createMembers(@RequestBody List<CreateMember> members) {
        return memberService.createMembers(members);
    }
}