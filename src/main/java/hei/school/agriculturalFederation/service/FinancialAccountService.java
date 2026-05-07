package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.entities.FinancialAccount;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.FinancialAccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FinancialAccountService {

    private final FinancialAccountRepository financialAccountRepository;
    private final CollectivityRepository collectivityRepository;

    public FinancialAccountService(FinancialAccountRepository financialAccountRepository,
                                   CollectivityRepository collectivityRepository) {
        this.financialAccountRepository = financialAccountRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<FinancialAccount> getAccountsByCollectivityIdAt(String collectivityId, LocalDate at) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        if (at == null) {
            throw new BadRequestException("Query parameter 'at' is mandatory.");
        }
        return financialAccountRepository.findAllByCollectivityIdAt(collectivityId, at);
    }
}