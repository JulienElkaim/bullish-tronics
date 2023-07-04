package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;

public class GlobalDeal extends Deal {
    private final Integer percent;

    public GlobalDeal(String dealId, String tagline, Instant startTime, Instant endTime, Integer percent) {
        super(dealId, DealType.GLOBAL_PERCENT, tagline, startTime, endTime);
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }
}
