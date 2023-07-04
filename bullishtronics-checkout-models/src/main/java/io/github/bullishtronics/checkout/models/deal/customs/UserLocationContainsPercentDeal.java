package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;

public class UserLocationContainsPercentDeal extends Deal {
    private final Integer percent;
    private final String locationSubtext;

    public UserLocationContainsPercentDeal(String dealId, String tagline, Instant startTime, Instant endTime, Integer percent, String locationSubtext) {
        super(dealId, DealType.USER_LOCATION_CONTAINS_PERCENT, tagline, startTime, endTime);
        this.percent = percent;
        this.locationSubtext = locationSubtext;
    }

    public Integer getPercent() {
        return percent;
    }

    public String getLocationSubtext() {
        return locationSubtext;
    }
}
