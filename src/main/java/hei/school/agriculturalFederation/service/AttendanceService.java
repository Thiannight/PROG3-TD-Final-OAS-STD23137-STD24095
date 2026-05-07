package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.exception.BadRequestException;
import hei.school.agriculturalFederation.exception.NotFoundException;
import hei.school.agriculturalFederation.model.entities.ActivityMemberAttendance;
import hei.school.agriculturalFederation.model.entities.CollectivityActivity;
import hei.school.agriculturalFederation.model.entities.Member;
import hei.school.agriculturalFederation.model.entities.MemberDescription;
import hei.school.agriculturalFederation.model.entities.MemberOccupation;
import hei.school.agriculturalFederation.model.enums.AttendanceStatus;
import hei.school.agriculturalFederation.model.requests.CreateActivityMemberAttendance;
import hei.school.agriculturalFederation.repository.ActivityRepository;
import hei.school.agriculturalFederation.repository.AttendanceRepository;
import hei.school.agriculturalFederation.repository.CollectivityRepository;
import hei.school.agriculturalFederation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ActivityRepository activityRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             ActivityRepository activityRepository,
                             CollectivityRepository collectivityRepository,
                             MemberRepository memberRepository) {
        this.attendanceRepository = attendanceRepository;
        this.activityRepository = activityRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<ActivityMemberAttendance> getAttendance(String collectivityId, String activityId) {
        assertCollectivityAndActivityExist(collectivityId, activityId);
        return attendanceRepository.findAllByActivityId(activityId);
    }

    public List<ActivityMemberAttendance> createAttendance(
            String collectivityId,
            String activityId,
            List<CreateActivityMemberAttendance> requests) {

        assertCollectivityAndActivityExist(collectivityId, activityId);

        CollectivityActivity activity = activityRepository.findById(activityId).orElseThrow();

        List<ActivityMemberAttendance> result = new ArrayList<>();
        for (CreateActivityMemberAttendance req : requests) {
            result.add(processOne(collectivityId, activityId, activity, req));
        }
        return result;
    }

    private ActivityMemberAttendance processOne(String collectivityId,
                                                String activityId,
                                                CollectivityActivity activity,
                                                CreateActivityMemberAttendance req) {
        if (req.getMemberIdentifier() == null) {
            throw new BadRequestException("memberIdentifier is required.");
        }
        if (req.getAttendanceStatus() == null) {
            throw new BadRequestException("attendanceStatus is required.");
        }

        Member member = memberRepository.findById(req.getMemberIdentifier())
                .orElseThrow(() -> new NotFoundException(
                        "Member not found: " + req.getMemberIdentifier()));

        boolean isInCollectivity = collectivityId.equals(member.getCollectivityId());
        boolean isConcerned = isMemberConcerned(member, activity);

        if (!isInCollectivity || !isConcerned) {
            if (req.getAttendanceStatus() != AttendanceStatus.ATTENDED) {
                throw new BadRequestException(
                        "Member " + req.getMemberIdentifier()
                                + " is not required for this activity. "
                                + "Only ATTENDED status can be set for optional participants.");
            }
        }

        ActivityMemberAttendance attendance = new ActivityMemberAttendance();
        attendance.setId(UUID.randomUUID().toString());
        attendance.setAttendanceStatus(req.getAttendanceStatus());
        attendance.setActivityDate(
                activity.getExecutiveDate() != null
                        ? activity.getExecutiveDate()
                        : LocalDate.now()
        );

        MemberDescription desc = new MemberDescription();
        desc.setId(member.getId());
        desc.setFirstName(member.getFirstName());
        desc.setLastName(member.getLastName());
        desc.setEmail(member.getEmail());
        desc.setOccupation(member.getOccupation());
        attendance.setMemberDescription(desc);

        return attendanceRepository.upsert(activityId, member.getId(), attendance);
    }

    private boolean isMemberConcerned(Member member, CollectivityActivity activity) {
        List<MemberOccupation> concerned = activity.getMemberOccupationConcerned();
        if (concerned == null || concerned.isEmpty()) {
            return true;
        }
        return concerned.contains(member.getOccupation());
    }

    private void assertCollectivityAndActivityExist(String collectivityId, String activityId) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }
        activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found: " + activityId));
    }
}