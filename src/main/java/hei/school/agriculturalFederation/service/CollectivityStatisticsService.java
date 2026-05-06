package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.CollectivityStatistics;
import hei.school.agriculturalFederation.model.Member;
import hei.school.agriculturalFederation.model.MemberPaymentStat;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.MemberRepository;
import hei.school.agriculturalFederation.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CollectivityStatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityStatisticsService(StatisticsRepository statisticsRepository,
                                         CollectivityRepository collectivityRepository,
                                         MemberRepository memberRepository) {
        this.statisticsRepository = statisticsRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public CollectivityStatistics getStatistics(String collectivityId, LocalDate from, LocalDate to) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        if (from == null || to == null) {
            throw new BadRequestException("Query parameters 'from' and 'to' are mandatory.");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' date must not be after 'to' date.");
        }

        Map<String, Double> paidByMember = statisticsRepository.getTotalPaidByMember(collectivityId, from, to);

        double expectedPerMember = statisticsRepository.getExpectedTotalFromActiveFees(collectivityId, from, to);

        List<Member> members = memberRepository.findAllByCollectivityId(collectivityId);

        List<MemberPaymentStat> memberStats = new ArrayList<>();
        for (Member member : members) {
            double paid = paidByMember.getOrDefault(member.getId(), 0.0);
            double unpaid = Math.max(0, expectedPerMember - paid);

            MemberPaymentStat stat = new MemberPaymentStat();
            stat.setMember(member);
            stat.setTotalPaid(paid);
            stat.setTotalUnpaid(unpaid);
            memberStats.add(stat);
        }

        CollectivityStatistics statistics = new CollectivityStatistics();
        statistics.setCollectivityId(collectivityId);
        statistics.setMemberStats(memberStats);
        return statistics;
    }
}