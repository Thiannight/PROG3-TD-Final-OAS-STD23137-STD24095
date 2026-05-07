package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.entities.CollectivityActivity;
import hei.school.agriculturalFederation.model.entities.MemberOccupation;
import hei.school.agriculturalFederation.model.entities.MonthlyRecurrenceRule;
import hei.school.agriculturalFederation.model.enums.ActivityType;
import hei.school.agriculturalFederation.model.enums.DayOfWeek;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ActivityRepository {

    private final DataSourceConfig dataSourceConfig;

    public ActivityRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<CollectivityActivity> findAllByCollectivityId(String collectivityId) {
        String sql = "SELECT * FROM collectivity_activity WHERE collectivity_id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            List<CollectivityActivity> activities = new ArrayList<>();
            while (rs.next()) {
                activities.add(mapRow(rs));
            }
            return activities;
        } catch (SQLException e) {
            throw new RuntimeException("Error in findAllByCollectivityId activity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public Optional<CollectivityActivity> findById(String activityId) {
        String sql = "SELECT * FROM collectivity_activity WHERE id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error in findById activity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public CollectivityActivity save(String collectivityId, CollectivityActivity activity) {
        String sql = """
                INSERT INTO collectivity_activity
                  (id, collectivity_id, label, activity_type,
                   member_occupation_concerned,
                   recurrence_week_ordinal, recurrence_day_of_week,
                   executive_date)
                VALUES (?, ?, ?, CAST(? AS activity_type_enum),
                        ?,
                        ?, CAST(? AS day_of_week_enum),
                        ?)
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activity.getId());
            ps.setString(2, collectivityId);
            ps.setString(3, activity.getLabel());
            ps.setString(4, activity.getActivityType().name());

            String occupations = null;
            if (activity.getMemberOccupationConcerned() != null
                    && !activity.getMemberOccupationConcerned().isEmpty()) {
                occupations = activity.getMemberOccupationConcerned()
                        .stream()
                        .map(MemberOccupation::name)
                        .collect(Collectors.joining(","));
            }
            ps.setString(5, occupations);

            MonthlyRecurrenceRule rule = activity.getRecurrenceRule();
            if (rule != null) {
                ps.setInt(6, rule.getWeekOrdinal());
                ps.setString(7, rule.getDayOfWeek().name());
            } else {
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.OTHER);
            }

            LocalDate execDate = activity.getExecutiveDate();
            if (execDate != null) {
                ps.setDate(8, Date.valueOf(execDate));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.executeUpdate();
            return activity;
        } catch (SQLException e) {
            throw new RuntimeException("Error in save activity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    private CollectivityActivity mapRow(ResultSet rs) throws SQLException {
        CollectivityActivity activity = new CollectivityActivity();
        activity.setId(rs.getString("id"));
        activity.setLabel(rs.getString("label"));
        activity.setActivityType(ActivityType.valueOf(rs.getString("activity_type")));

        String occupationsRaw = rs.getString("member_occupation_concerned");
        if (occupationsRaw != null && !occupationsRaw.isBlank()) {
            List<MemberOccupation> occupations = Arrays.stream(occupationsRaw.split(","))
                    .map(String::trim)
                    .map(MemberOccupation::valueOf)
                    .collect(Collectors.toList());
            activity.setMemberOccupationConcerned(occupations);
        }

        int weekOrdinal = rs.getInt("recurrence_week_ordinal");
        String dayOfWeekStr = rs.getString("recurrence_day_of_week");
        if (!rs.wasNull() && dayOfWeekStr != null) {
            MonthlyRecurrenceRule rule = new MonthlyRecurrenceRule();
            rule.setWeekOrdinal(weekOrdinal);
            rule.setDayOfWeek(DayOfWeek.valueOf(dayOfWeekStr));
            activity.setRecurrenceRule(rule);
        }

        Date execDate = rs.getDate("executive_date");
        if (execDate != null) {
            activity.setExecutiveDate(execDate.toLocalDate());
        }

        return activity;
    }
}