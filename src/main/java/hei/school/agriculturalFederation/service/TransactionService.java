package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.CollectivityTransaction;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CollectivityRepository collectivityRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CollectivityRepository collectivityRepository) {
        this.transactionRepository = transactionRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityTransaction> getTransactions(String collectivityId,
                                                         LocalDate from, LocalDate to) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are mandatory.");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date must not be after 'to' date.");
        }
        return transactionRepository.findByCollectivityIdAndPeriod(collectivityId, from, to);
    }
}