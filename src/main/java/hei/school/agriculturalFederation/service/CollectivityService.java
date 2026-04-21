package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.*;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<Collectivity> createCollectivities(List<CreateCollectivity> requests) {
        List<Collectivity> created = new ArrayList<>();
        for (CreateCollectivity req : requests) {
            created.add(createOne(req));
        }
        return created;
    }

    private Collectivity createOne(CreateCollectivity req) {

        if (!req.isFederationApproval()) {
            throw new BadRequestException(
                    "Formal federation approval is required to open a collectivity.");
        }

        if (req.getStructure() == null) {
            throw new BadRequestException(
                    "The structure (president, vice president, treasurer, secretary) is mandatory.");
        }

        CreateCollectivityStructure structureReq = req.getStructure();
        if (structureReq.getPresident() == null
                || structureReq.getVicePresident() == null
                || structureReq.getTreasurer() == null
                || structureReq.getSecretary() == null) {
            throw new BadRequestException(
                    "All positions in the structure must be filled (president, vice president, treasurer, secretary).");
        }

        Set<String> allIds = new HashSet<>();
        if (req.getMembers() != null) {
            allIds.addAll(req.getMembers());
        }
        allIds.add(structureReq.getPresident());
        allIds.add(structureReq.getVicePresident());
        allIds.add(structureReq.getTreasurer());
        allIds.add(structureReq.getSecretary());

        List<Member> resolvedMembers = new ArrayList<>();
        for (String memberId : allIds) {
            Member m = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException(
                            "Member not found: " + memberId));
            resolvedMembers.add(m);
        }

        if (resolvedMembers.size() < 10) {
            throw new BadRequestException(
                    "A collectivity must have at least 10 members (currently: "
                            + resolvedMembers.size() + ").");
        }

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        long seniorCount = resolvedMembers.stream()
                .filter(m -> m.getMembershipDate() != null
                        && !m.getMembershipDate().isAfter(sixMonthsAgo))
                .count();

        if (seniorCount < 5) {
            throw new BadRequestException(
                    "At least 5 members must have a seniority of at least 6 months "
                            + "within the federation (currently: " + seniorCount + ").");
        }

        String newId = UUID.randomUUID().toString();

        Collectivity collectivity = collectivityRepository.save(
                newId,
                req.getLocation(),
                req.isFederationApproval(),
                structureReq.getPresident(),
                structureReq.getVicePresident(),
                structureReq.getTreasurer(),
                structureReq.getSecretary()
        );

        collectivityRepository.updateMemberCollectivity(new ArrayList<>(allIds), newId);

        return collectivity;
    }
}