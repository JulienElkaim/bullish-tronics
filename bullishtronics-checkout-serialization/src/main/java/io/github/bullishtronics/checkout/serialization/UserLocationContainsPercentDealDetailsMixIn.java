package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class UserLocationContainsPercentDealDetailsMixIn {
    @JsonCreator
    public UserLocationContainsPercentDealDetailsMixIn(@JsonProperty("tagline") String tagline,
                                                       @JsonProperty("startTime") Instant startTime,
                                                       @JsonProperty("endTime") Instant endTime,
                                                       @JsonProperty("percent") Integer percent,
                                                       @JsonProperty("locationSubtext") String locationSubtext) {

    }
}
