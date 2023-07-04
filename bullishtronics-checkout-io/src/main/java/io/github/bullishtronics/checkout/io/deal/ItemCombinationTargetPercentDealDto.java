package io.github.bullishtronics.checkout.io.deal;

import java.time.Instant;
import java.util.Set;

public class ItemCombinationTargetPercentDealDto extends DealDto {
    private final String targetProductId;
    private final Set<String> productIdsToCombine;
    private final Integer percent;

    public ItemCombinationTargetPercentDealDto(String dealId, String targetProductId, Integer percent, String tagline, Instant startTime, Instant endTime, Set<String> productIdsToCombine) {
        super(dealId, DealTypeDto.ITEM_COMBINATION_TARGET_PERCENT, tagline, startTime, endTime);
        this.targetProductId = targetProductId;
        this.productIdsToCombine = Set.copyOf(productIdsToCombine);
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }

    public String getTargetProductId() {
        return targetProductId;
    }

    public Set<String> getProductIdsToCombine() {
        return productIdsToCombine;
    }
}
