package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;

public class GlobalDealDetails extends DealDetails {
    private final Integer percent;
    public GlobalDealDetails(String tagline, Instant startTime, Instant endTime, Integer percent) {
        super(tagline, DealType.GLOBAL_PERCENT,  startTime, endTime);
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }
}
