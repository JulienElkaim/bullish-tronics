package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;

public class UserLocationContainsPercentDealDetails extends DealDetails {
    private final Integer percent;
    private final String locationSubtext;
    public UserLocationContainsPercentDealDetails(String tagline, Instant startTime, Instant endTime, Integer percent, String locationSubtext) {
        super(tagline, DealType.USER_LOCATION_CONTAINS_PERCENT,  startTime, endTime);
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
