package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ItemCombinationTargetPercentDeal extends Deal {
    private final String targetProductId;
    private final Set<String> productIdsToCombine;
    private final Integer percent;

    public ItemCombinationTargetPercentDeal(String dealId, String targetProductId, Integer percent, String tagline, Instant startTime, Instant endTime, Set<String> productIdsToCombine) {
        super(dealId, DealType.ITEM_COMBINATION_TARGET_PERCENT, tagline, startTime, endTime);
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
