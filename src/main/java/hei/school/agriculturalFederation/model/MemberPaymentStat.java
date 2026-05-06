package hei.school.agriculturalFederation.model;

public class MemberPaymentStat {
    private Member member;
    private double totalPaid;
    private double totalUnpaid;

    public MemberPaymentStat() {}

    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }

    public double getTotalPaid() {
        return totalPaid;
    }
    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getTotalUnpaid() {
        return totalUnpaid;
    }
    public void setTotalUnpaid(double totalUnpaid) {
        this.totalUnpaid = totalUnpaid;
    }
}