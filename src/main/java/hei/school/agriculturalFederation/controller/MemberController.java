package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.requests.CreateMember;
import hei.school.agriculturalFederation.model.requests.CreateMemberPayment;
import hei.school.agriculturalFederation.model.entities.Member;
import hei.school.agriculturalFederation.model.entities.MemberPayment;
import hei.school.agriculturalFederation.service.MemberPaymentService;
import hei.school.agriculturalFederation.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberPaymentService memberPaymentService;

    public MemberController(MemberService memberService,
                            MemberPaymentService memberPaymentService) {
        this.memberService = memberService;
        this.memberPaymentService = memberPaymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Member> createMembers(@RequestBody List<CreateMember> members) {
        return memberService.createMembers(members);
    }

    @PostMapping("/{id}/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public List<MemberPayment> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPayment> payments) {
        return memberPaymentService.createPayments(id, payments);
    }
}