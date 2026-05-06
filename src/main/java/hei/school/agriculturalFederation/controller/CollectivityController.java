package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.entities.*;
import hei.school.agriculturalFederation.model.requests.CreateCollectivity;
import hei.school.agriculturalFederation.model.requests.CreateMembershipFee;
import hei.school.agriculturalFederation.service.CollectivityService;
import hei.school.agriculturalFederation.service.FinancialAccountService;
import hei.school.agriculturalFederation.service.MembershipFeeService;
import hei.school.agriculturalFederation.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;
    private final MembershipFeeService membershipFeeService;
    private final TransactionService transactionService;
    private final FinancialAccountService financialAccountService;

    public CollectivityController(CollectivityService collectivityService,
                                  MembershipFeeService membershipFeeService,
                                  TransactionService transactionService,
                                  FinancialAccountService financialAccountService) {
        this.collectivityService = collectivityService;
        this.membershipFeeService = membershipFeeService;
        this.transactionService = transactionService;
        this.financialAccountService = financialAccountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Collectivity> createCollectivities(@RequestBody List<CreateCollectivity> collectivities) {
        return collectivityService.createCollectivities(collectivities);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Collectivity getCollectivityById(@PathVariable String id) {
        return collectivityService.getById(id);
    }

    @PutMapping("/{id}/informations")
    @ResponseStatus(HttpStatus.OK)
    public Collectivity updateInformations(
            @PathVariable String id,
            @RequestBody CollectivityInformation request) {
        return collectivityService.updateInformations(id, request);
    }

    @GetMapping("/{id}/membershipFees")
    @ResponseStatus(HttpStatus.OK)
    public List<MembershipFee> getMembershipFees(@PathVariable String id) {
        return membershipFeeService.getByCollectivityId(id);
    }

    @PostMapping("/{id}/membershipFees")
    @ResponseStatus(HttpStatus.OK)
    public List<MembershipFee> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> fees) {
        return membershipFeeService.createMembershipFees(id, fees);
    }

    @GetMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<CollectivityTransaction> getTransactions(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return transactionService.getTransactions(id, from, to);
    }

    @GetMapping("/{id}/financialAccounts")
    @ResponseStatus(HttpStatus.OK)
    public List<FinancialAccount> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at) {
        return financialAccountService.getAccountsByCollectivityIdAt(id, at);
    }
}