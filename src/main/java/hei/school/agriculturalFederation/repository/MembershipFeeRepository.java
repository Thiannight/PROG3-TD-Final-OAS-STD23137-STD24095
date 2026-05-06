package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.entities.MembershipFee;
import hei.school.agriculturalFederation.model.enums.ActivityStatus;
import hei.school.agriculturalFederation.model.enums.Frequency;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MembershipFeeRepository {

    private final DataSourceConfig dataSourceConfig;

    public MembershipFeeRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<MembershipFee> findAllByCollectivityId(String collectivityId) {
        String sql = "SELECT * FROM membership_fee WHERE collectivity_id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            List<MembershipFee> fees = new ArrayList<>();
            while (rs.next()) {
                fees.add(mapRow(rs));
            }
            return fees;
        } catch (SQLException e) {
            throw new RuntimeException("Error in findAllByCollectivityId membership_fee: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public Optional<MembershipFee> findById(String id) {
        String sql = "SELECT * FROM membership_fee WHERE id = ?";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error in findById membership_fee: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public MembershipFee save(String collectivityId, MembershipFee fee) {
        String sql = """
                INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status)
                VALUES (?, ?, ?, CAST(? AS frequency_enum), ?, ?, CAST(? AS activity_status_enum))
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fee.getId());
            ps.setString(2, collectivityId);
            ps.setDate(3, Date.valueOf(fee.getEligibleFrom()));
            ps.setString(4, fee.getFrequency().name());
            ps.setDouble(5, fee.getAmount());
            ps.setString(6, fee.getLabel());
            ps.setString(7, fee.getStatus().name());
            ps.executeUpdate();
            return fee;
        } catch (SQLException e) {
            throw new RuntimeException("Error in save membership_fee: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    private MembershipFee mapRow(ResultSet rs) throws SQLException {
        MembershipFee fee = new MembershipFee();
        fee.setId(rs.getString("id"));
        fee.setEligibleFrom(rs.getObject("eligible_from", LocalDate.class));
        fee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
        fee.setAmount(rs.getDouble("amount"));
        fee.setLabel(rs.getString("label"));
        fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
        return fee;
    }
}