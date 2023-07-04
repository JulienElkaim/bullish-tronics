package io.github.bullishtronics.checkout.io.order;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;

import java.util.List;

public class OrderDto {
    private final String orderId;
    private final String userId;
    private final List<ProductPurchaseDetailsDto> productPurchaseDetailsList;
    private final List<DiscountDto> globalDiscounts;

    public OrderDto(String orderId, String userId, List<ProductPurchaseDetailsDto> productPurchaseDetailsList, List<DiscountDto> globalDiscounts) {
        this.orderId = orderId;
        this.userId = userId;
        this.productPurchaseDetailsList = List.copyOf(productPurchaseDetailsList);
        this.globalDiscounts = List.copyOf(globalDiscounts);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<ProductPurchaseDetailsDto> getItemPurchaseDetailsList() {
        return productPurchaseDetailsList;
    }

    public List<DiscountDto> getGlobalDiscounts() {
        return globalDiscounts;
    }
}
