package io.github.bullishtronics.checkout.models.order;

import io.github.bullishtronics.checkout.models.deal.Discount;

import java.util.List;
import java.util.Map;

public class QuotationDetails {
    private final Map<String,Integer> productsByQuantity;
    private final List<Discount> discounts;

    public QuotationDetails(Map<String, Integer> productsByQuantity, List<Discount> discounts) {
        this.productsByQuantity = Map.copyOf(productsByQuantity);
        this.discounts = List.copyOf(discounts);
    }

    public Map<String, Integer> getProductsByQuantity() {
        return productsByQuantity;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
