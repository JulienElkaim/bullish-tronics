package io.github.bullishtronics.checkout.io.product;

public class DeleteProductReport {
    private final String productId;
    private final Boolean isSuccess;
    private final String details;

    public static DeleteProductReport success(String productId) {
        return new DeleteProductReport(productId, true, null);
    }

    public static DeleteProductReport failure(String productId, String details) {
        return new DeleteProductReport(productId, false, details);
    }

    private DeleteProductReport(String productId, Boolean isSuccess, String details) {
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
