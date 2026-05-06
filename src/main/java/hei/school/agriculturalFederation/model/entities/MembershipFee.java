package hei.school.agriculturalFederation.model.entities;

import hei.school.agriculturalFederation.model.enums.ActivityStatus;
import hei.school.agriculturalFederation.model.requests.CreateMembershipFee;

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