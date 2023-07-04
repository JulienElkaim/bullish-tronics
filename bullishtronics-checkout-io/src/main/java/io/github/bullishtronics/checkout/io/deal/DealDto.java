package io.github.bullishtronics.checkout.io.deal;

import java.time.Instant;

public abstract class DealDto {
    private final String dealId;
    private final DealTypeDto type;
    private final String tagline;
    private final Instant startTime;
    private final Instant endTime;

    public DealDto(String dealId, DealTypeDto type, String tagline, Instant startTime, Instant endTime) {
        this.dealId = dealId;
        this.type = type;
        this.tagline = tagline;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDealId() {
        return dealId;
    }

    public DealTypeDto getType() {
        return type;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public String getTagline() {
        return tagline;
    }
}
