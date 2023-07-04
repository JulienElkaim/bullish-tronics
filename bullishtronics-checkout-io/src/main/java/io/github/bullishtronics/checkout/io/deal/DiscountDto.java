package io.github.bullishtronics.checkout.io.deal;

import java.math.BigDecimal;

public class DiscountDto {
    private final String productId;
    private final BigDecimal amount;
    private final String dealITagLine;
    private final boolean isStackable;

    public DiscountDto(String productId, BigDecimal amount, String dealITagLine, boolean isStackable) {
        this.productId = productId;
        this.amount = amount;
        this.dealITagLine = dealITagLine;
        this.isStackable = isStackable;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDealTagline() {
        return dealITagLine;
    }

    public boolean isStackable() {
        return isStackable;
    }
}
