package hei.school.agriculturalFederation.model.entities;

import hei.school.agriculturalFederation.model.enums.AttendanceStatus;

import java.time.LocalDate;

public class ActivityMemberAttendance {
    private String id;
    private MemberDescription memberDescription;
    private AttendanceStatus attendanceStatus;
    private LocalDate activityDate;

    public ActivityMemberAttendance() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public MemberDescription getMemberDescription() { return memberDescription; }
    public void setMemberDescription(MemberDescription memberDescription) {
        this.memberDescription = memberDescription;
    }

    public AttendanceStatus getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
}