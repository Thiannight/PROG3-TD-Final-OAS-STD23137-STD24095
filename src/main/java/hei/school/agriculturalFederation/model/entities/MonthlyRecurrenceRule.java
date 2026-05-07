package hei.school.agriculturalFederation.model.entities;

import hei.school.agriculturalFederation.model.enums.DayOfWeek;

public class MonthlyRecurrenceRule {
    private int weekOrdinal;
    private DayOfWeek dayOfWeek;

    public MonthlyRecurrenceRule() {}

    public int getWeekOrdinal() {
        return weekOrdinal;
    }
    public void setWeekOrdinal(int weekOrdinal) {
        this.weekOrdinal = weekOrdinal;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}