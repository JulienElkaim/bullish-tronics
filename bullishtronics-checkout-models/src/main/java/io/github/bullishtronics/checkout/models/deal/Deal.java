package io.github.bullishtronics.checkout.models.deal;

import java.time.Instant;

public abstract class Deal {
    private final String dealId;
    private final DealType type;
    private final String tagline;
    private final Instant startTime;
    private final Instant endTime;

    public Deal(String dealId, DealType type, String tagline, Instant startTime, Instant endTime) {
        this.dealId = dealId;
        this.type = type;
        this.tagline = tagline;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDealId() {
        return dealId;
    }

    public DealType getType() {
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
