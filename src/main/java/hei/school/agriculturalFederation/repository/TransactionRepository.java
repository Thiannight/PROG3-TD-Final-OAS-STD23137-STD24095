package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.entities.CollectivityTransaction;
import hei.school.agriculturalFederation.model.enums.PaymentMode;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    private final DataSourceConfig dataSourceConfig;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository financialAccountRepository;

    public TransactionRepository(DataSourceConfig dataSourceConfig,
                                 MemberRepository memberRepository,
                                 FinancialAccountRepository financialAccountRepository) {
        this.dataSourceConfig = dataSourceConfig;
        this.memberRepository = memberRepository;
        this.financialAccountRepository = financialAccountRepository;
    }

    public CollectivityTransaction save(String collectivityId, CollectivityTransaction transaction) {
        String sql = """
                INSERT INTO collectivity_transaction
                  (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id)
                VALUES (?, ?, ?, ?, CAST(? AS payment_mode_enum), ?, ?)
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transaction.getId());
            ps.setString(2, collectivityId);
            ps.setDate(3, Date.valueOf(transaction.getCreationDate()));
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getPaymentMode().name());
            ps.setString(6, transaction.getAccountCredited().getId());
            ps.setString(7, transaction.getMemberDebited().getId());
            ps.executeUpdate();
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException("Error in save transaction: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public List<CollectivityTransaction> findByCollectivityIdAndPeriod(
            String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT * FROM collectivity_transaction
                WHERE collectivity_id = ? AND creation_date >= ? AND creation_date <= ?
                ORDER BY creation_date
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            List<CollectivityTransaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Error in findByCollectivityIdAndPeriod: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    private CollectivityTransaction mapRow(ResultSet rs) throws SQLException {
        CollectivityTransaction t = new CollectivityTransaction();
        t.setId(rs.getString("id"));
        t.setCreationDate(rs.getObject("creation_date", LocalDate.class));
        t.setAmount(rs.getDouble("amount"));
        t.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));

        String accountId = rs.getString("account_credited_id");
        financialAccountRepository.findById(accountId).ifPresent(t::setAccountCredited);

        String memberId = rs.getString("member_debited_id");
        memberRepository.findById(memberId).ifPresent(t::setMemberDebited);

        return t;
    }
}
