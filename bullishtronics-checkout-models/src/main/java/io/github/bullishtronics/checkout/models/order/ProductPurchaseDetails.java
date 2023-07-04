package io.github.bullishtronics.checkout.models.order;

import io.github.bullishtronics.checkout.models.deal.Discount;

import java.math.BigDecimal;
import java.util.List;

public class ProductPurchaseDetails {
    private final String productId;
    private final String productName;
    private final BigDecimal rawPricePerUnit;
    private final int quantity;
    private final List<Discount> itemSpecificDiscounts;

    public ProductPurchaseDetails(String productId, String productName, BigDecimal rawPricePerUnit, int quantity, List<Discount> itemSpecificDiscounts) {
        this.productId = productId;
        this.productName = productName;
        this.rawPricePerUnit = rawPricePerUnit;
        this.quantity = quantity;
        this.itemSpecificDiscounts = List.copyOf(itemSpecificDiscounts);
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getRawPricePerUnit() {
        return rawPricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Discount> getItemSpecificDiscounts() {
        return itemSpecificDiscounts;
    }
}
