package io.github.bullishtronics.checkout.io.order;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;

import java.util.List;
import java.util.Map;

public class QuotationDetailsDto {
    private final Map<String,Integer> productsByQuantity;
    private final List<DiscountDto> discounts;

    public QuotationDetailsDto(Map<String, Integer> productsByQuantity, List<DiscountDto> discounts) {
        this.productsByQuantity = Map.copyOf(productsByQuantity);
        this.discounts = List.copyOf(discounts);
    }

    public Map<String, Integer> getProductsByQuantity() {
        return productsByQuantity;
    }

    public List<DiscountDto> getDiscounts() {
        return discounts;
    }
}
