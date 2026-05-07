package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.entities.CollectivityLocalStatistics;
import hei.school.agriculturalFederation.model.entities.CollectivityOverallStatistics;
import hei.school.agriculturalFederation.service.CollectivityStatisticsService;
import hei.school.agriculturalFederation.service.FederationStatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class StatisticsController {

    private final CollectivityStatisticsService collectivityStatisticsService;
    private final FederationStatisticsService federationStatisticsService;

    public StatisticsController(CollectivityStatisticsService collectivityStatisticsService,
                                FederationStatisticsService federationStatisticsService) {
        this.collectivityStatisticsService = collectivityStatisticsService;
        this.federationStatisticsService = federationStatisticsService;
    }

    @GetMapping("/collectivites/statistics")
    @ResponseStatus(HttpStatus.OK)
    public List<CollectivityOverallStatistics> getFederationStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return federationStatisticsService.getOverallStatistics(from, to);
    }

    @GetMapping("/collectivites/{id}/statistics")
    @ResponseStatus(HttpStatus.OK)
    public List<CollectivityLocalStatistics> getCollectivityStatistics(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return collectivityStatisticsService.getLocalStatistics(id, from, to);
    }
}