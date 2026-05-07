package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatisticsRepository {

    private final DataSourceConfig dataSourceConfig;

    public StatisticsRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public Map<String, Double> getTotalPaidByMember(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT mc.member_id, COALESCE(SUM(mp.amount), 0) AS total_paid
                FROM member_collectivity mc
                LEFT JOIN member_payment mp
                    ON mp.member_id = mc.member_id
                    AND mp.creation_date >= ?
                    AND mp.creation_date <= ?
                WHERE mc.collectivity_id = ?
                GROUP BY mc.member_id
                """;
        Map<String, Double> result = new HashMap<>();
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("member_id"), rs.getDouble("total_paid"));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getTotalPaidByMember: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public double getExpectedTotalFromActiveFees(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT COALESCE(SUM(amount), 0)
                FROM membership_fee
                WHERE collectivity_id = ?
                  AND status = 'ACTIVE'
                  AND eligible_from <= ?
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getExpectedTotalFromActiveFees: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public List<String> getAllCollectivityIds() {
        String sql = "SELECT id FROM collectivity";
        List<String> ids = new ArrayList<>();
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getString("id"));
            return ids;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getAllCollectivityIds: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public String getCollectivityName(String collectivityId) {
        String sql = "SELECT name FROM collectivity WHERE id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("name");
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getCollectivityName: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public Integer getCollectivityNumber(String collectivityId) {
        String sql = "SELECT number FROM collectivity WHERE id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String number = rs.getString("number");
                return number != null ? Integer.parseInt(number) : null;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getCollectivityNumber: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public long countNewMembers(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT COUNT(*)
                FROM member_collectivity
                WHERE collectivity_id = ?
                  AND adhesion_date >= ?
                  AND adhesion_date <= ?
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error in countNewMembers: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public long countTotalMembers(String collectivityId) {
        String sql = "SELECT COUNT(*) FROM member_collectivity WHERE collectivity_id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error in countTotalMembers: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public long countUpToDateMembers(String collectivityId, LocalDate from, LocalDate to) {
        double expectedAmount = getExpectedTotalFromActiveFees(collectivityId, from, to);

        if (expectedAmount <= 0) {
            return countTotalMembers(collectivityId);
        }

        String sql = """
                SELECT COUNT(*) FROM (
                    SELECT mc.member_id, COALESCE(SUM(mp.amount), 0) AS total_paid
                    FROM member_collectivity mc
                    LEFT JOIN member_payment mp
                        ON mp.member_id = mc.member_id
                        AND mp.creation_date >= ?
                        AND mp.creation_date <= ?
                    WHERE mc.collectivity_id = ?
                    GROUP BY mc.member_id
                    HAVING COALESCE(SUM(mp.amount), 0) >= ?
                ) AS up_to_date
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ps.setDouble(4, expectedAmount);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error in countUpToDateMembers: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public double getMemberAssiduityPercentage(String memberId, String collectivityId) {
        String sql = """
                SELECT
                    COUNT(CASE WHEN aa.attendance_status = 'ATTENDED' THEN 1 END) AS attended,
                    COUNT(CASE WHEN aa.attendance_status = 'MISSING'  THEN 1 END) AS missing
                FROM activity_attendance aa
                JOIN collectivity_activity ca ON ca.id = aa.activity_id
                JOIN member_collectivity mc ON mc.member_id = aa.member_id
                                           AND mc.collectivity_id = ca.collectivity_id
                WHERE aa.member_id = ?
                  AND ca.collectivity_id = ?
                  AND aa.attendance_status IN ('ATTENDED', 'MISSING')
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long attended = rs.getLong("attended");
                long missing  = rs.getLong("missing");
                long total = attended + missing;
                if (total == 0) return 100.0; // aucune activité confirmée → 100 %
                return Math.round((double) attended / total * 10000.0) / 100.0;
            }
            return 100.0;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getMemberAssiduityPercentage: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public double getCollectivityAssiduityPercentage(String collectivityId) {
        String sql = """
                SELECT
                    mc.member_id,
                    COUNT(CASE WHEN aa.attendance_status = 'ATTENDED' THEN 1 END) AS attended,
                    COUNT(CASE WHEN aa.attendance_status = 'MISSING'  THEN 1 END) AS missing
                FROM member_collectivity mc
                LEFT JOIN activity_attendance aa ON aa.member_id = mc.member_id
                LEFT JOIN collectivity_activity ca ON ca.id = aa.activity_id
                                                  AND ca.collectivity_id = mc.collectivity_id
                                                  AND aa.attendance_status IN ('ATTENDED', 'MISSING')
                WHERE mc.collectivity_id = ?
                GROUP BY mc.member_id
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            double totalRate = 0.0;
            int memberCount = 0;
            while (rs.next()) {
                long attended = rs.getLong("attended");
                long missing  = rs.getLong("missing");
                long total = attended + missing;
                double rate = (total == 0) ? 100.0 : (double) attended / total * 100.0;
                totalRate += rate;
                memberCount++;
            }
            if (memberCount == 0) return 100.0;
            return Math.round(totalRate / memberCount * 100.0) / 100.0;
        } catch (SQLException e) {
            throw new RuntimeException("Error in getCollectivityAssiduityPercentage: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }
}