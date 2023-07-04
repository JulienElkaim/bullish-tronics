package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;

public class ItemModuloLastPercentDeal extends Deal {
    private final String productId;
    private final Integer moduloNumber;
    private final Integer percent;

    public ItemModuloLastPercentDeal(String dealId, String productId, Integer moduloNumber, Integer percent, String tagline, Instant startTime, Instant endTime) {
        super(dealId, DealType.ITEM_MODULO_LAST_ITEM_PERCENT, tagline, startTime, endTime);
        this.productId = productId;
        this.moduloNumber = moduloNumber;
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getModuloNumber() {
        return moduloNumber;
    }
}
