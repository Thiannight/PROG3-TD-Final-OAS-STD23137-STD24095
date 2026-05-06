package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.entities.BankAccount;
import hei.school.agriculturalFederation.model.entities.CashAccount;
import hei.school.agriculturalFederation.model.entities.FinancialAccount;
import hei.school.agriculturalFederation.model.entities.MobileBankingAccount;
import hei.school.agriculturalFederation.model.enums.Bank;
import hei.school.agriculturalFederation.model.enums.MobileBankingService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FinancialAccountRepository {

    private final DataSourceConfig dataSourceConfig;

    public FinancialAccountRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public Optional<FinancialAccount> findById(String id) {
        Connection conn = dataSourceConfig.getConnection();
        try {
            // 1. Cash
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM cash_account WHERE id = ?")) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    CashAccount acc = new CashAccount();
                    acc.setId(rs.getString("id"));
                    acc.setAmount(rs.getDouble("amount"));
                    return Optional.of(acc);
                }
            }
            // 2. Mobile banking
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM mobile_banking_account WHERE id = ?")) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    MobileBankingAccount acc = new MobileBankingAccount();
                    acc.setId(rs.getString("id"));
                    acc.setHolderName(rs.getString("holder_name"));
                    acc.setMobileBankingService(
                            MobileBankingService.valueOf(rs.getString("mobile_banking_service")));
                    acc.setMobileNumber(rs.getLong("mobile_number"));
                    acc.setAmount(rs.getDouble("amount"));
                    return Optional.of(acc);
                }
            }
            // 3. Bank account
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM bank_account WHERE id = ?")) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    BankAccount acc = new BankAccount();
                    acc.setId(rs.getString("id"));
                    acc.setHolderName(rs.getString("holder_name"));
                    acc.setBankName(Bank.valueOf(rs.getString("bank_name")));
                    acc.setBankCode(rs.getInt("bank_code"));
                    acc.setBankBranchCode(rs.getInt("bank_branch_code"));
                    acc.setBankAccountNumber(rs.getInt("bank_account_number"));
                    acc.setBankAccountKey(rs.getInt("bank_account_key"));
                    acc.setAmount(rs.getDouble("amount"));
                    return Optional.of(acc);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error querying financial account: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public List<FinancialAccount> findAllByCollectivityIdAt(String collectivityId, LocalDate at) {
        String sql = "SELECT account_id FROM collectivity_account WHERE collectivity_id = ?";

        List<String> accountIds = new ArrayList<>();
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accountIds.add(rs.getString("account_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching account ids for collectivity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }

        List<FinancialAccount> accounts = new ArrayList<>();
        for (String accountId : accountIds) {
            findById(accountId).ifPresent(acc -> {
                double creditAfterAt = getCreditedAfterDate(accountId, collectivityId, at);
                acc.setAmount(acc.getAmount() - creditAfterAt);
                accounts.add(acc);
            });
        }

        return accounts;
    }

    private double getCreditedAfterDate(String accountId, String collectivityId, LocalDate at) {
        String sql = """
                SELECT COALESCE(SUM(amount), 0)
                FROM collectivity_transaction
                WHERE account_credited_id = ?
                  AND collectivity_id = ?
                  AND creation_date > ?
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, collectivityId);
            ps.setDate(3, Date.valueOf(at));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating credit after date: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }


    public String findCollectivityIdByAccountId(String accountId) {
        String sql = "SELECT collectivity_id FROM collectivity_account WHERE account_id = ? LIMIT 1";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("collectivity_id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error in findCollectivityIdByAccountId: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }
    public void creditAccount(String accountId, double amount) {
        Connection conn = dataSourceConfig.getConnection();
        try {
            String[] tables = {"cash_account", "mobile_banking_account", "bank_account"};
            for (String table : tables) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE " + table + " SET amount = amount + ? WHERE id = ?")) {
                    ps.setDouble(1, amount);
                    ps.setString(2, accountId);
                    if (ps.executeUpdate() > 0) return;
                }
            }
            throw new RuntimeException("Financial account not found: " + accountId);
        } catch (SQLException e) {
            throw new RuntimeException("Error crediting account: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }
}