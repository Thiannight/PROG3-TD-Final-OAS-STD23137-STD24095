package hei.school.agriculturalFederation.model.requests;

import hei.school.agriculturalFederation.model.enums.Frequency;

import java.time.LocalDate;

public class CreateMembershipFee {
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private double amount;
    private String label;

    public CreateMembershipFee() {}

    public LocalDate getEligibleFrom() {
        return eligibleFrom;
    }
    public void setEligibleFrom(LocalDate eligibleFrom) {
        this.eligibleFrom = eligibleFrom;
    }

    public Frequency getFrequency() {
        return frequency;
    }
    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}