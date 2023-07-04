package io.github.bullishtronics.checkout.io.deal;

import java.time.Instant;

public class UserLocationContainsPercentDealDto extends DealDto {
    private final Integer percent;
    private final String locationSubtext;

    public UserLocationContainsPercentDealDto(String dealId, String tagline, Instant startTime, Instant endTime, Integer percent, String locationSubtext) {
        super(dealId, DealTypeDto.USER_LOCATION_CONTAINS_PERCENT, tagline, startTime, endTime);
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
