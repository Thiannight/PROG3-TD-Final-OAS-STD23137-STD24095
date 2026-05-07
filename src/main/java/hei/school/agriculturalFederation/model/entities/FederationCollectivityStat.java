package hei.school.agriculturalFederation.model.entities;

public class FederationCollectivityStat {
    private String collectivityId;
    private String collectivityName;
    private double upToDateMembersPercentage;
    private long newMembersCount;

    public FederationCollectivityStat() {}

    public String getCollectivityId() {
        return collectivityId;
    }
    public void setCollectivityId(String collectivityId) {
        this.collectivityId = collectivityId;
    }

    public String getCollectivityName() {
        return collectivityName;
    }
    public void setCollectivityName(String collectivityName) {
        this.collectivityName = collectivityName;
    }

    public double getUpToDateMembersPercentage() {
        return upToDateMembersPercentage;
    }
    public void setUpToDateMembersPercentage(double upToDateMembersPercentage) {
        this.upToDateMembersPercentage = upToDateMembersPercentage;
    }

    public long getNewMembersCount() {
        return newMembersCount;
    }
    public void setNewMembersCount(long newMembersCount) {
        this.newMembersCount = newMembersCount;
    }
}