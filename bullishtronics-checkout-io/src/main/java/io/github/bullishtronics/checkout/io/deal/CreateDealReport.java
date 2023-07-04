package io.github.bullishtronics.checkout.io.deal;

public class CreateDealReport {
    private final String dealId;
    private final Boolean isSuccess;
    private final String details;

    public static CreateDealReport success(String productId) {
        return new CreateDealReport(productId, true, null);
    }

    public static CreateDealReport failure(String details) {
        return new CreateDealReport(null, false, details);
    }

    private CreateDealReport(String dealId, Boolean isSuccess, String details) {
        this.dealId = dealId;
        this.isSuccess = isSuccess;
        this.details = details;
    }

    public String getDealId() {
        return dealId;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getDetails() {
        return details;
    }
}