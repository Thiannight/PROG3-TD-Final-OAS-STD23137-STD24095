package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class FinancialAccountRepository {

    private final DataSourceConfig dataSourceConfig;

    public FinancialAccountRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public Optional<FinancialAccount> findById(String id) {
        Connection conn = dataSourceConfig.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM cash_account WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CashAccount acc = new CashAccount();
                acc.setId(rs.getString("id"));
                acc.setAmount(rs.getDouble("amount"));
                return Optional.of(acc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying cash_account: " + e.getMessage(), e);
        }

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM mobile_banking_account WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MobileBankingAccount acc = new MobileBankingAccount();
                acc.setId(rs.getString("id"));
                acc.setHolderName(rs.getString("holder_name"));
                acc.setMobileBankingService(MobileBankingService.valueOf(rs.getString("mobile_banking_service")));
                acc.setMobileNumber(rs.getLong("mobile_number"));
                acc.setAmount(rs.getDouble("amount"));
                return Optional.of(acc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying mobile_banking_account: " + e.getMessage(), e);
        }

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM bank_account WHERE id = ?")) {
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
        } catch (SQLException e) {
            throw new RuntimeException("Error querying bank_account: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }

        return Optional.empty();
    }

    public void creditAccount(String accountId, double amount) {
        Connection conn = dataSourceConfig.getConnection();

        String[] tables = {"cash_account", "mobile_banking_account", "bank_account"};
        for (String table : tables) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE " + table + " SET amount = amount + ? WHERE id = ?")) {
                ps.setDouble(1, amount);
                ps.setString(2, accountId);
                if (ps.executeUpdate() > 0) return;
            } catch (SQLException e) {
                throw new RuntimeException("Error crediting account in " + table + ": " + e.getMessage(), e);
            }
        }

        dataSourceConfig.closeConnection(conn);
        throw new RuntimeException("Financial account not found: " + accountId);
    }
}