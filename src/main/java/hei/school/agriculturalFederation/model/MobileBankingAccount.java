package hei.school.agriculturalFederation.model;

public class MobileBankingAccount extends FinancialAccount {
    private String holderName;
    private MobileBankingService mobileBankingService;
    private long mobileNumber;

    public MobileBankingAccount() {}

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public MobileBankingService getMobileBankingService() { return mobileBankingService; }
    public void setMobileBankingService(MobileBankingService mobileBankingService) {
        this.mobileBankingService = mobileBankingService;
    }

    public long getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(long mobileNumber) { this.mobileNumber = mobileNumber; }
}