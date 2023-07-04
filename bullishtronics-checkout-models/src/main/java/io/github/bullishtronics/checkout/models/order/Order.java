package io.github.bullishtronics.checkout.models.order;

import io.github.bullishtronics.checkout.models.deal.Discount;

import java.util.List;

public class Order {
    private final String orderId;
    private final String userId;
    private final List<ProductPurchaseDetails> productPurchaseDetails;
    private final List<Discount> globalDiscounts;

    public Order(String orderId, String userId, List<ProductPurchaseDetails> productPurchaseDetails, List<Discount> globalDiscounts) {
        this.orderId = orderId;
        this.userId = userId;
        this.productPurchaseDetails = List.copyOf(productPurchaseDetails);
        this.globalDiscounts = List.copyOf(globalDiscounts);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<ProductPurchaseDetails> getProductPurchaseDetails() {
        return productPurchaseDetails;
    }

    public List<Discount> getGlobalDiscounts() {
        return globalDiscounts;
    }
}
