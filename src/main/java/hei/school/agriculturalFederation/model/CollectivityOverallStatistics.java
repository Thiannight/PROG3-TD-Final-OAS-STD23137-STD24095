package hei.school.agriculturalFederation.model;

public class CollectivityOverallStatistics {
    private CollectivityInformation collectivityInformation;
    private int newMembersNumber;
    private double overallMemberCurrentDuePercentage;

    public CollectivityOverallStatistics() {}

    public CollectivityInformation getCollectivityInformation() {
        return collectivityInformation;
    }
    public void setCollectivityInformation(CollectivityInformation collectivityInformation) {
        this.collectivityInformation = collectivityInformation;
    }

    public int getNewMembersNumber() {
        return newMembersNumber;
    }
    public void setNewMembersNumber(int newMembersNumber) {
        this.newMembersNumber = newMembersNumber;
    }

    public double getOverallMemberCurrentDuePercentage() {
        return overallMemberCurrentDuePercentage;
    }
    public void setOverallMemberCurrentDuePercentage(double overallMemberCurrentDuePercentage) {
        this.overallMemberCurrentDuePercentage = overallMemberCurrentDuePercentage;
    }
}
