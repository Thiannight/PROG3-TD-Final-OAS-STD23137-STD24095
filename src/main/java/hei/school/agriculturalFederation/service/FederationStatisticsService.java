package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.model.FederationCollectivityStat;
import hei.school.agriculturalFederation.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FederationStatisticsService {

    private final StatisticsRepository statisticsRepository;

    public FederationStatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<FederationCollectivityStat> getStatistics(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are mandatory.");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date must not be after 'to' date.");
        }

        List<String> collectivityIds = statisticsRepository.getAllCollectivityIds();
        List<FederationCollectivityStat> result = new ArrayList<>();

        for (String collectivityId : collectivityIds) {
            long totalMembers = statisticsRepository.countTotalMembers(collectivityId);
            long upToDate = statisticsRepository.countUpToDateMembers(collectivityId, from, to);
            long newMembers = statisticsRepository.countNewMembers(collectivityId, from, to);
            String name = statisticsRepository.getCollectivityName(collectivityId);

            double percentage = totalMembers > 0
                    ? (double) upToDate / totalMembers * 100.0
                    : 0.0;

            FederationCollectivityStat stat = new FederationCollectivityStat();
            stat.setCollectivityId(collectivityId);
            stat.setCollectivityName(name);
            stat.setUpToDateMembersPercentage(Math.round(percentage * 100.0) / 100.0);
            stat.setNewMembersCount(newMembers);
            result.add(stat);
        }

        return result;
    }
}