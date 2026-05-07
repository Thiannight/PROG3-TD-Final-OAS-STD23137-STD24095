package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.entities.CollectivityActivity;
import hei.school.agriculturalFederation.model.entities.Member;
import hei.school.agriculturalFederation.model.entities.MemberOccupation;
import hei.school.agriculturalFederation.model.requests.CreateCollectivityActivity;
import hei.school.agriculturalFederation.repository.ActivityRepository;
import hei.school.agriculturalFederation.repository.AttendanceRepository;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public ActivityService(ActivityRepository activityRepository,
                           AttendanceRepository attendanceRepository,
                           CollectivityRepository collectivityRepository,
                           MemberRepository memberRepository) {
        this.activityRepository = activityRepository;
        this.attendanceRepository = attendanceRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<CollectivityActivity> getActivities(String collectivityId) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        return activityRepository.findAllByCollectivityId(collectivityId);
    }

    public List<CollectivityActivity> createActivities(String collectivityId,
                                                       List<CreateCollectivityActivity> requests) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        List<CollectivityActivity> created = new ArrayList<>();
        for (CreateCollectivityActivity req : requests) {
            created.add(createOne(collectivityId, req));
        }
        return created;
    }

    private CollectivityActivity createOne(String collectivityId, CreateCollectivityActivity req) {
        if (req.getRecurrenceRule() != null && req.getExecutiveDate() != null) {
            throw new BadRequestException(
                    "Cannot provide both recurrenceRule and executiveDate at the same time.");
        }
        if (req.getRecurrenceRule() == null && req.getExecutiveDate() == null) {
            throw new BadRequestException(
                    "Either recurrenceRule or executiveDate must be provided.");
        }
        if (req.getActivityType() == null) {
            throw new BadRequestException("activityType is required.");
        }
        if (req.getRecurrenceRule() != null) {
            int wo = req.getRecurrenceRule().getWeekOrdinal();
            if (wo < 1 || wo > 5) {
                throw new BadRequestException("weekOrdinal must be between 1 and 5.");
            }
        }

        CollectivityActivity activity = new CollectivityActivity();
        activity.setId(UUID.randomUUID().toString());
        activity.setLabel(req.getLabel());
        activity.setActivityType(req.getActivityType());
        activity.setMemberOccupationConcerned(req.getMemberOccupationConcerned());
        activity.setRecurrenceRule(req.getRecurrenceRule());
        activity.setExecutiveDate(req.getExecutiveDate());

        activityRepository.save(collectivityId, activity);

        List<Member> allMembers = memberRepository.findAllByCollectivityId(collectivityId);
        List<String> concernedMemberIds;

        List<MemberOccupation> concernedOccupations = req.getMemberOccupationConcerned();
        if (concernedOccupations == null || concernedOccupations.isEmpty()) {
            concernedMemberIds = allMembers.stream()
                    .map(Member::getId)
                    .collect(Collectors.toList());
        } else {
            concernedMemberIds = allMembers.stream()
                    .filter(m -> concernedOccupations.contains(m.getOccupation()))
                    .map(Member::getId)
                    .collect(Collectors.toList());
        }

        attendanceRepository.initUndefinedForMembers(activity.getId(), concernedMemberIds);

        return activity;
    }
}