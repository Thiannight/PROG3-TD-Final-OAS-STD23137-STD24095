package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.model.entities.CollectivityInformation;
import hei.school.agriculturalFederation.model.entities.CollectivityOverallStatistics;
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

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are mandatory.");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date must not be after 'to' date.");
        }

        List<String> collectivityIds = statisticsRepository.getAllCollectivityIds();
        List<CollectivityOverallStatistics> result = new ArrayList<>();

        for (String collectivityId : collectivityIds) {
            long totalMembers = statisticsRepository.countTotalMembers(collectivityId);
            long upToDate = statisticsRepository.countUpToDateMembers(collectivityId, from, to);
            long newMembers = statisticsRepository.countNewMembers(collectivityId, from, to);

            double percentage = totalMembers > 0
                    ? Math.round((double) upToDate / totalMembers * 10000.0) / 100.0
                    : 0.0;

            CollectivityInformation info = new CollectivityInformation();
            info.setName(statisticsRepository.getCollectivityName(collectivityId));
            info.setNumber(statisticsRepository.getCollectivityNumber(collectivityId));

            CollectivityOverallStatistics stat = new CollectivityOverallStatistics();
            stat.setCollectivityInformation(info);
            stat.setNewMembersNumber((int) newMembers);
            stat.setOverallMemberCurrentDuePercentage(percentage);
            result.add(stat);
        }

        return result;
    }
}