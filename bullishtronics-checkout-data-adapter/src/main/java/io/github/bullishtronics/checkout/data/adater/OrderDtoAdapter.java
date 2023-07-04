package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;
import io.github.bullishtronics.checkout.io.order.OrderDto;
import io.github.bullishtronics.checkout.io.order.ProductPurchaseDetailsDto;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;

import java.util.List;

public class OrderDtoAdapter {
    private final ProductPurchaseDetailsDtoAdapter productPurchaseDetailsDtoAdapter;
    private final DiscountDtoAdapter discountDtoAdapter;

    public OrderDtoAdapter(ProductPurchaseDetailsDtoAdapter productPurchaseDetailsDtoAdapter, DiscountDtoAdapter discountDtoAdapter) {
        this.productPurchaseDetailsDtoAdapter = productPurchaseDetailsDtoAdapter;
        this.discountDtoAdapter = discountDtoAdapter;
    }

    public OrderDto adapte(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getUserId(),
                buildProductPurchaseDetails(order.getProductPurchaseDetails()),
                buildGlobalDiscount(order.getGlobalDiscounts())
        );
    }

    private List<DiscountDto> buildGlobalDiscount(List<Discount> globalDiscounts) {
        return globalDiscounts == null ? List.of() : globalDiscounts.stream()
                .map(discountDtoAdapter::adapte)
                .toList();
    }

    private List<ProductPurchaseDetailsDto> buildProductPurchaseDetails(List<ProductPurchaseDetails> productPurchaseDetails) {
        return productPurchaseDetails == null ? List.of() : productPurchaseDetails.stream()
                .map(productPurchaseDetailsDtoAdapter::adapte)
                .toList();
    }
}
