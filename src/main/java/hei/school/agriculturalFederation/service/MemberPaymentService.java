package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.*;
import hei.school.agriculturalFederation.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberPaymentService {

    private final MemberPaymentRepository memberPaymentRepository;
    private final MemberRepository memberRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final TransactionRepository transactionRepository;
    private final CollectivityRepository collectivityRepository;

    public MemberPaymentService(MemberPaymentRepository memberPaymentRepository,
                                MemberRepository memberRepository,
                                MembershipFeeRepository membershipFeeRepository,
                                FinancialAccountRepository financialAccountRepository,
                                TransactionRepository transactionRepository,
                                CollectivityRepository collectivityRepository) {
        this.memberPaymentRepository = memberPaymentRepository;
        this.memberRepository = memberRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.financialAccountRepository = financialAccountRepository;
        this.transactionRepository = transactionRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> requests) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found: " + memberId));

        List<MemberPayment> created = new ArrayList<>();
        for (CreateMemberPayment req : requests) {
            created.add(createOne(member, req));
        }
        return created;
    }

    private MemberPayment createOne(Member member, CreateMemberPayment req) {
        if (req.getAmount() <= 0) {
            throw new BadRequestException("Payment amount must be greater than 0.");
        }

        membershipFeeRepository.findById(req.getMembershipFeeIdentifier())
                .orElseThrow(() -> new NotFoundException(
                        "Membership fee not found: " + req.getMembershipFeeIdentifier()));

        FinancialAccount account = financialAccountRepository
                .findById(req.getAccountCreditedIdentifier())
                .orElseThrow(() -> new NotFoundException(
                        "Financial account not found: " + req.getAccountCreditedIdentifier()));

        financialAccountRepository.creditAccount(account.getId(), req.getAmount());
        account.setAmount(account.getAmount() + req.getAmount());

        MemberPayment payment = new MemberPayment();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmount(req.getAmount());
        payment.setPaymentMode(req.getPaymentMode());
        payment.setAccountCredited(account);
        payment.setCreationDate(LocalDate.now());

        memberPaymentRepository.save(member.getId(), payment);


        String collectivityId = financialAccountRepository
                .findCollectivityIdByAccountId(account.getId());
        if (collectivityId == null) {
            // Fallback: use the member's primary collectivity
            collectivityId = member.getCollectivityId();
        }

        CollectivityTransaction transaction = new CollectivityTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setCreationDate(LocalDate.now());
        transaction.setAmount(req.getAmount());
        transaction.setPaymentMode(req.getPaymentMode());
        transaction.setAccountCredited(account);
        transaction.setMemberDebited(member);

        transactionRepository.save(collectivityId, transaction);

        return payment;
    }
}