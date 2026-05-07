package hei.school.agriculturalFederation.model.requests;

import hei.school.agriculturalFederation.model.enums.AttendanceStatus;

public class CreateActivityMemberAttendance {
    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;

    public CreateActivityMemberAttendance() {}

    public String getMemberIdentifier() { return memberIdentifier; }
    public void setMemberIdentifier(String memberIdentifier) {
        this.memberIdentifier = memberIdentifier;
    }

    public AttendanceStatus getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}