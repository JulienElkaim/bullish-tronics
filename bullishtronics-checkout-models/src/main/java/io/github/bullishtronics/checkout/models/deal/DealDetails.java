package io.github.bullishtronics.checkout.models.deal;

import java.time.Instant;

public abstract class DealDetails {
    private final String tagline;
    private final Instant startTime;
    private final Instant endTime;
    private final DealType dealType;

    public DealDetails(String tagline, DealType dealtType, Instant startTime, Instant endTime) {
        this.tagline = tagline;
        this.dealType = dealtType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTagline() {
        return tagline;
    }

    public DealType getDealType() {
        return dealType;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }
}
