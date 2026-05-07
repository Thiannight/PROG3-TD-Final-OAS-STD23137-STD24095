package hei.school.agriculturalFederation.model.entities;

import hei.school.agriculturalFederation.model.enums.Bank;

public class BankAccount extends FinancialAccount {
    private String holderName;
    private Bank bankName;
    private int bankCode;
    private int bankBranchCode;
    private int bankAccountNumber;
    private int bankAccountKey;

    public BankAccount() {}

    public String getHolderName() {
        return holderName;
    }
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Bank getBankName() {
        return bankName;
    }
    public void setBankName(Bank bankName) {
        this.bankName = bankName;
    }

    public int getBankCode() {
        return bankCode;
    }
    public void setBankCode(int bankCode) {
        this.bankCode = bankCode;
    }

    public int getBankBranchCode() {
        return bankBranchCode;
    }
    public void setBankBranchCode(int bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public int getBankAccountNumber() {
        return bankAccountNumber;
    }
    public void setBankAccountNumber(int bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public int getBankAccountKey() {
        return bankAccountKey;
    }
    public void setBankAccountKey(int bankAccountKey) {
        this.bankAccountKey = bankAccountKey;
    }
}