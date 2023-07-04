package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class GlobalDealDetailsMixIn {
    @JsonCreator
    public GlobalDealDetailsMixIn(@JsonProperty("tagLine") String tagline,
                                  @JsonProperty("startTime") Instant startTime,
                                  @JsonProperty("endTime") Instant endTime,
                                  @JsonProperty("percent") Integer percent) {
    }
}
