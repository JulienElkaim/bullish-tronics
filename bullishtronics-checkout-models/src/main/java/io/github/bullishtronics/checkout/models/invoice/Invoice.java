package io.github.bullishtronics.checkout.models.invoice;

import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;

import java.math.BigDecimal;
import java.util.List;

public class Invoice {
    private final String orderId;
    private final String userName;
    private final String userAddress;
    private final List<ProductPurchaseDetails> productPurchaseDetails;
    private final List<Discount> globalDiscounts;
    private final BigDecimal totalPrice;

    public Invoice(String orderId, String userName, String userAddress, List<ProductPurchaseDetails> productPurchaseDetails, List<Discount> globalDiscounts, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.userName = userName;
        this.userAddress = userAddress;
        this.productPurchaseDetails = List.copyOf(productPurchaseDetails);
        this.globalDiscounts = List.copyOf(globalDiscounts);
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public List<ProductPurchaseDetails> getProductPurchaseDetails() {
        return productPurchaseDetails;
    }

    public List<Discount> getGlobalDiscounts() {
        return globalDiscounts;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
