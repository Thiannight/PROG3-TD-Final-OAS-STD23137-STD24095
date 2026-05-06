package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.entities.MemberPayment;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class MemberPaymentRepository {

    private final DataSourceConfig dataSourceConfig;
    private final FinancialAccountRepository financialAccountRepository;

    public MemberPaymentRepository(DataSourceConfig dataSourceConfig,
                                   FinancialAccountRepository financialAccountRepository) {
        this.dataSourceConfig = dataSourceConfig;
        this.financialAccountRepository = financialAccountRepository;
    }

    public MemberPayment save(String memberId, MemberPayment payment) {
        String sql = """
                INSERT INTO member_payment
                  (id, member_id, amount, payment_mode, account_credited_id, creation_date)
                VALUES (?, ?, ?, CAST(? AS payment_mode_enum), ?, ?)
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, payment.getId());
            ps.setString(2, memberId);
            ps.setLong(3, payment.getAmount());
            ps.setString(4, payment.getPaymentMode().name());
            ps.setString(5, payment.getAccountCredited().getId());
            ps.setDate(6, Date.valueOf(payment.getCreationDate()));
            ps.executeUpdate();
            return payment;
        } catch (SQLException e) {
            throw new RuntimeException("Error in save member_payment: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }
}
