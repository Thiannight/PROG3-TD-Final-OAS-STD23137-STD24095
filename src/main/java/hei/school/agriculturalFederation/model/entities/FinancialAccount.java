package hei.school.agriculturalFederation.model.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CashAccount.class, name = "CASH"),
        @JsonSubTypes.Type(value = MobileBankingAccount.class, name = "MOBILE_BANKING"),
        @JsonSubTypes.Type(value = BankAccount.class, name = "BANK_TRANSFER")
})
public abstract class FinancialAccount {
    private String id;
    private double amount;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}