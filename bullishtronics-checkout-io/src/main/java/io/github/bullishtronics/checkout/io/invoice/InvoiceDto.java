package io.github.bullishtronics.checkout.io.invoice;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;
import io.github.bullishtronics.checkout.io.order.ProductPurchaseDetailsDto;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceDto {
    private final String orderId;
    private final String userName;
    private final String userAddress;
    private final List<ProductPurchaseDetailsDto> productPurchaseDetails;
    private final List<DiscountDto> globalDiscounts;
    private final BigDecimal totalPrice;

    public InvoiceDto(String orderId, String userName, String userAddress, List<ProductPurchaseDetailsDto> productPurchaseDetails, List<DiscountDto> globalDiscounts, BigDecimal totalPrice) {
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

    public List<ProductPurchaseDetailsDto> getProductPurchaseDetails() {
        return productPurchaseDetails;
    }

    public List<DiscountDto> getGlobalDiscounts() {
        return globalDiscounts;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
