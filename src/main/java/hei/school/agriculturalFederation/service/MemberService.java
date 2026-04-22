package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.*;
import hei.school.agriculturalFederation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> createMembers(List<CreateMember> requests) {
        List<Member> created = new ArrayList<>();
        for (CreateMember req : requests) {
            created.add(createOne(req));
        }
        return created;
    }

    private Member createOne(CreateMember req) {

        if (!memberRepository.collectivityExists(req.getCollectivityIdentifier())) {
            throw new NotFoundException(
                    "Collectivity not found: " + req.getCollectivityIdentifier());
        }

        if (!req.isRegistrationFeePaid()) {
            throw new BadRequestException(
                    "The registration fee (50,000 Ar) has not been paid.");
        }
        if (!req.isMembershipDuesPaid()) {
            throw new BadRequestException(
                    "The mandatory annual membership dues have not been paid.");
        }

        long requiredDues = memberRepository.getCollectivityAnnualDues(req.getCollectivityIdentifier());
        if (req.getMembershipDuesAmount() < requiredDues) {
            throw new BadRequestException(
                    "Insufficient annual dues payment. Required: " + requiredDues
                            + " Ar, provided: " + req.getMembershipDuesAmount() + " Ar.");
        }

        List<String> refereeIds = req.getReferees();
        if (refereeIds == null || refereeIds.size() < 2) {
            throw new BadRequestException(
                    "The candidate must be sponsored by at least 2 confirmed members.");
        }

        List<Member> referees = new ArrayList<>();
        for (String refereeId : refereeIds) {
            Member referee = memberRepository.findById(refereeId)
                    .orElseThrow(() -> new NotFoundException(
                            "Sponsor not found: " + refereeId));
            referees.add(referee);
        }

        for (Member referee : referees) {
            if (!isConfirmed(referee.getOccupation())) {
                throw new BadRequestException(
                        "Sponsor " + referee.getId()
                                + " is not a confirmed member (SENIOR or higher).");
            }
        }

        LocalDate limit = LocalDate.now().minusDays(90);
        for (Member referee : referees) {
            if (referee.getMembershipDate() == null
                    || referee.getMembershipDate().isAfter(limit)) {
                throw new BadRequestException(
                        "Sponsor " + referee.getId()
                                + " does not yet have 90 days of seniority.");
            }
        }

        String targetCollectivity = req.getCollectivityIdentifier();
        long internalCount = referees.stream()
                .filter(r -> targetCollectivity.equals(r.getCollectivityId()))
                .count();
        long externalCount = referees.size() - internalCount;

        if (internalCount < externalCount) {
            throw new BadRequestException(
                    "The number of sponsors from the target collectivity ("
                            + internalCount + ") must be at least equal "
                            + "to the number of external sponsors (" + externalCount + ").");
        }

        Member member = new Member();
        member.setId(UUID.randomUUID().toString());
        member.setFirstName(req.getFirstName());
        member.setLastName(req.getLastName());
        member.setBirthDate(req.getBirthDate());
        member.setGender(req.getGender());
        member.setAddress(req.getAddress());
        member.setProfession(req.getProfession());
        member.setPhoneNumber(req.getPhoneNumber());
        member.setEmail(req.getEmail());
        member.setOccupation(MemberOccupation.JUNIOR);
        member.setMembershipDate(LocalDate.now());
        member.setCollectivityId(targetCollectivity);
        member.setReferees(referees);

        memberRepository.save(member);

        for (Member referee : referees) {
            memberRepository.saveSponsorship(member.getId(), referee.getId());
        }

        return member;
    }

    private boolean isConfirmed(MemberOccupation occupation) {
        return occupation == MemberOccupation.SENIOR
                || occupation == MemberOccupation.SECRETARY
                || occupation == MemberOccupation.TREASURER
                || occupation == MemberOccupation.VICE_PRESIDENT
                || occupation == MemberOccupation.PRESIDENT;
    }
}