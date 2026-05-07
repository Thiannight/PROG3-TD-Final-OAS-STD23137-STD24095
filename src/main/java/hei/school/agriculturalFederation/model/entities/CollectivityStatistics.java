package hei.school.agriculturalFederation.model.entities;

import java.util.List;

public class CollectivityStatistics {
    private String collectivityId;
    private List<MemberPaymentStat> memberStats;

    public CollectivityStatistics() {}

    public String getCollectivityId() {
        return collectivityId;
    }
    public void setCollectivityId(String collectivityId) {
        this.collectivityId = collectivityId;
    }

    public List<MemberPaymentStat> getMemberStats() {
        return memberStats;
    }
    public void setMemberStats(List<MemberPaymentStat> memberStats) {
        this.memberStats = memberStats;
    }
}