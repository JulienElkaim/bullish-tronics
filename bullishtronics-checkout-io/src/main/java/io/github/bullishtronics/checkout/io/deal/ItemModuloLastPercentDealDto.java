package io.github.bullishtronics.checkout.io.deal;

import java.time.Instant;

public class ItemModuloLastPercentDealDto extends DealDto {
    private final String productId;
    private final Integer moduloNumber;
    private final Integer percent;

    public ItemModuloLastPercentDealDto(String dealId, String tagline, Instant startTime, Instant endTime, String productId, Integer moduloNumber, Integer percent) {
        super(dealId, DealTypeDto.ITEM_MODULO_LAST_ITEM_PERCENT, tagline, startTime, endTime);
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
