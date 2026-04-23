package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.*;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.MembershipFeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectivityRepository collectivityRepository;

    public MembershipFeeService(MembershipFeeRepository membershipFeeRepository,
                                CollectivityRepository collectivityRepository) {
        this.membershipFeeRepository = membershipFeeRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<MembershipFee> getByCollectivityId(String collectivityId) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        return membershipFeeRepository.findAllByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createMembershipFees(String collectivityId,
                                                    List<CreateMembershipFee> requests) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        List<MembershipFee> created = new ArrayList<>();
        for (CreateMembershipFee req : requests) {
            if (req.getFrequency() == null) {
                throw new BadRequestException("Unrecognized or missing frequency.");
            }
            if (req.getAmount() < 0) {
                throw new BadRequestException("Amount must be greater than or equal to 0.");
            }

            MembershipFee fee = new MembershipFee();
            fee.setId(UUID.randomUUID().toString());
            fee.setEligibleFrom(req.getEligibleFrom());
            fee.setFrequency(req.getFrequency());
            fee.setAmount(req.getAmount());
            fee.setLabel(req.getLabel());
            fee.setStatus(ActivityStatus.ACTIVE);

            created.add(membershipFeeRepository.save(collectivityId, fee));
        }
        return created;
    }
}