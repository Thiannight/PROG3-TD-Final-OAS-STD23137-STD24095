package hei.school.agriculturalFederation.model.entities;

import hei.school.agriculturalFederation.model.enums.ActivityType;
import hei.school.agriculturalFederation.model.enums.TargetAudience;

import java.time.LocalDate;

public class Activity {
    private String id;
    private String collectivityId;  // null for federation-level activities
    private String title;
    private ActivityType type;
    private LocalDate scheduledDate;
    private boolean mandatory;
    private TargetAudience targetAudience;
    private String planificationRule; // e.g. "SECOND_SUNDAY_MONTHLY"

    public Activity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCollectivityId() { return collectivityId; }
    public void setCollectivityId(String collectivityId) { this.collectivityId = collectivityId; }

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