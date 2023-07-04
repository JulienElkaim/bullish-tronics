package io.github.bullishtronics.checkout.io.deal;

import java.time.Instant;

public class GlobalDealDto extends DealDto {
    private final Integer percent;

    public GlobalDealDto(String dealId, String tagline, Instant startTime, Instant endTime, Integer percent) {
        super(dealId, DealTypeDto.GLOBAL_PERCENT, tagline, startTime, endTime);
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }
}
