package hei.school.agriculturalFederation.model.requests;

import hei.school.agriculturalFederation.model.enums.ActivityType;
import hei.school.agriculturalFederation.model.enums.TargetAudience;

import java.time.LocalDate;

public class CreateActivity {
    private String title;
    private ActivityType type;
    private LocalDate scheduledDate;
    private boolean mandatory;
    private TargetAudience targetAudience;
    private String planificationRule;

    public CreateActivity() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }

    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }

    public TargetAudience getTargetAudience() { return targetAudience; }
    public void setTargetAudience(TargetAudience targetAudience) { this.targetAudience = targetAudience; }

    public String getPlanificationRule() { return planificationRule; }
    public void setPlanificationRule(String planificationRule) { this.planificationRule = planificationRule; }
}