package io.github.bullishtronics.checkout.io.product;

public class CreateProductReport {
    private final String productId;
    private final Boolean isSuccess;
    private final String details;

    public static CreateProductReport success(String productId) {
        return new CreateProductReport(productId, true, null);
    }

    public static CreateProductReport failure(String details) {
        return new CreateProductReport(null, false, details);
    }

    private CreateProductReport(String productId, Boolean isSuccess, String details) {
        this.productId = productId;
        this.isSuccess = isSuccess;
        this.details = details;
    }

    public String getProductId() {
        return productId;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getDetails() {
        return details;
    }
}
