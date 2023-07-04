package io.github.bullishtronics.checkout.io.order;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;

import java.math.BigDecimal;
import java.util.List;

public class ProductPurchaseDetailsDto {
    private final String productId;
    private final String productName;
    private final BigDecimal rawPricePerUnit;
    private final int quantity;
    private final List<DiscountDto> itemSpecificDiscounts;

    public ProductPurchaseDetailsDto(String productId, String productName, BigDecimal rawPricePerUnit, int quantity, List<DiscountDto> itemSpecificDiscounts) {
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

    public List<DiscountDto> getItemSpecificDiscounts() {
        return itemSpecificDiscounts;
    }
}
