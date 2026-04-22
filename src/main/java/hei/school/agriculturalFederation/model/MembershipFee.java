package hei.school.agriculturalFederation.model;

import java.time.LocalDate;

public class MembershipFee extends CreateMembershipFee {
    private String id;
    private ActivityStatus status;

    public MembershipFee() {}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public ActivityStatus getStatus() {
        return status;
    }
    public void setStatus(ActivityStatus status) {
        this.status = status;
    }
}