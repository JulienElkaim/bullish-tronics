package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public class ItemCombinationTargetPercentDealDetailsMixIn {
    @JsonCreator
    public ItemCombinationTargetPercentDealDetailsMixIn(@JsonProperty("targetProductId") String targetProductId,
                                                        @JsonProperty("percent") Integer percent,
                                                        @JsonProperty("tagline") String tagline,
                                                        @JsonProperty("startTime") Instant startTime,
                                                        @JsonProperty("endTime") Instant endTime,
                                                        @JsonProperty("productIdsToCombine") Set<String> productIdsToCombine) {
    }
}
