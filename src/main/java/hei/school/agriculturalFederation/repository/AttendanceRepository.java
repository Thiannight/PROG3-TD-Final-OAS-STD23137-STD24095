package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.entities.ActivityMemberAttendance;
import hei.school.agriculturalFederation.model.entities.MemberDescription;
import hei.school.agriculturalFederation.model.entities.MemberOccupation;
import hei.school.agriculturalFederation.model.enums.AttendanceStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AttendanceRepository {

    private final DataSourceConfig dataSourceConfig;

    public AttendanceRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<ActivityMemberAttendance> findAllByActivityId(String activityId) {
        String sql = """
                SELECT a.id, a.attendance_status,
                       m.id AS member_id, m.first_name, m.last_name, m.email,
                       mc.occupation
                FROM activity_attendance a
                JOIN member m ON m.id = a.member_id
                LEFT JOIN member_collectivity mc ON mc.member_id = m.id
                WHERE a.activity_id = ?
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            List<ActivityMemberAttendance> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error in findAllByActivityId attendance: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public Optional<ActivityMemberAttendance> findByActivityIdAndMemberId(String activityId, String memberId) {
        String sql = """
                SELECT a.id, a.attendance_status,
                       m.id AS member_id, m.first_name, m.last_name, m.email,
                       mc.occupation
                FROM activity_attendance a
                JOIN member m ON m.id = a.member_id
                LEFT JOIN member_collectivity mc ON mc.member_id = m.id
                WHERE a.activity_id = ? AND a.member_id = ?
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ps.setString(2, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error in findByActivityIdAndMemberId: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public ActivityMemberAttendance upsert(String activityId, String memberId,
                                           ActivityMemberAttendance attendance) {
        Optional<ActivityMemberAttendance> existing =
                findByActivityIdAndMemberId(activityId, memberId);

        if (existing.isPresent()) {
            AttendanceStatus currentStatus = existing.get().getAttendanceStatus();
            if (currentStatus != AttendanceStatus.UNDEFINED) {
                throw new hei.school.agriculturalFederation.exception.BadRequestException(
                        "Attendance for member " + memberId
                                + " is already confirmed as " + currentStatus
                                + " and cannot be modified.");
            }
            String sql = """
                    UPDATE activity_attendance
                    SET attendance_status = CAST(? AS attendance_status_enum)
                    WHERE activity_id = ? AND member_id = ?
                    """;
            Connection conn = dataSourceConfig.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, attendance.getAttendanceStatus().name());
                ps.setString(2, activityId);
                ps.setString(3, memberId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating attendance: " + e.getMessage(), e);
            } finally {
                dataSourceConfig.closeConnection(conn);
            }
            attendance.setId(existing.get().getId());
        } else {
            String sql = """
                    INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
                    VALUES (?, ?, ?, CAST(? AS attendance_status_enum), ?)
                    """;
            Connection conn = dataSourceConfig.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, attendance.getId());
                ps.setString(2, activityId);
                ps.setString(3, memberId);
                ps.setString(4, attendance.getAttendanceStatus().name());
                ps.setDate(5, Date.valueOf(attendance.getActivityDate()));
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting attendance: " + e.getMessage(), e);
            } finally {
                dataSourceConfig.closeConnection(conn);
            }
        }
        return attendance;
    }

    public void initUndefinedForMembers(String activityId, List<String> memberIds, LocalDate activityDate) {
        if (memberIds == null || memberIds.isEmpty()) return;
        String sql = """
                INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
                VALUES (?, ?, ?, CAST('UNDEFINED' AS attendance_status_enum), ?)
                ON CONFLICT (activity_id, member_id, activity_date) DO NOTHING
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String memberId : memberIds) {
                ps.setString(1, java.util.UUID.randomUUID().toString());
                ps.setString(2, activityId);
                ps.setString(3, memberId);
                ps.setDate(4, Date.valueOf(activityDate));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error in initUndefinedForMembers: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    private ActivityMemberAttendance mapRow(ResultSet rs) throws SQLException {
        ActivityMemberAttendance att = new ActivityMemberAttendance();
        att.setId(rs.getString("id"));
        att.setAttendanceStatus(AttendanceStatus.valueOf(rs.getString("attendance_status")));

        MemberDescription desc = new MemberDescription();
        desc.setId(rs.getString("member_id"));
        desc.setFirstName(rs.getString("first_name"));
        desc.setLastName(rs.getString("last_name"));
        desc.setEmail(rs.getString("email"));
        String occ = rs.getString("occupation");
        if (occ != null) {
            desc.setOccupation(MemberOccupation.valueOf(occ));
        }
        att.setMemberDescription(desc);
        return att;
    }
}