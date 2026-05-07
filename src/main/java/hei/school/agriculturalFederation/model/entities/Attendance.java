package hei.school.agriculturalFederation.model.entities;

import hei.school.agriculturalFederation.model.enums.AttendanceStatus;

import java.time.LocalDate;

public class Attendance {
    private String id;
    private String activityId;
    private Member member;
    private AttendanceStatus status;
    private LocalDate recordedAt;

    public Attendance() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public LocalDate getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDate recordedAt) { this.recordedAt = recordedAt; }
}