package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ItemModuloLastPercentDealDetailsMixIn {
    @JsonCreator
    public ItemModuloLastPercentDealDetailsMixIn(@JsonProperty("productId") String productId,
                                                     @JsonProperty("moduloNumber") Integer moduloNumber,
                                                     @JsonProperty("percent") Integer percent,
                                                     @JsonProperty("tagLine") String tagline,
                                                     @JsonProperty("startTime") Instant startTime,
                                                     @JsonProperty("endTime") Instant endTime) {
    }
}
