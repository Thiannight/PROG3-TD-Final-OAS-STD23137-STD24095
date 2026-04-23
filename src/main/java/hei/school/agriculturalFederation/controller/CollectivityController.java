package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.*;
import hei.school.agriculturalFederation.service.CollectivityService;
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

    public CollectivityController(CollectivityService collectivityService,
                                  MembershipFeeService membershipFeeService,
                                  TransactionService transactionService) {
        this.collectivityService = collectivityService;
        this.membershipFeeService = membershipFeeService;
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Collectivity> createCollectivities(@RequestBody List<CreateCollectivity> collectivities) {
        return collectivityService.createCollectivities(collectivities);
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
}