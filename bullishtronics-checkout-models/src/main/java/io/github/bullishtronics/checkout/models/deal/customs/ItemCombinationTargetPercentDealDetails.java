package io.github.bullishtronics.checkout.models.deal.customs;

import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.time.Instant;
import java.util.Set;

public class ItemCombinationTargetPercentDealDetails extends DealDetails {
    private final String targetProductId;
    private final Set<String> productIdsToCombine;
    private final Integer percent;

    public ItemCombinationTargetPercentDealDetails(String targetProductId, Integer percent, String tagline, Instant startTime, Instant endTime, Set<String> productIdsToCombine) {
        super(tagline, DealType.ITEM_COMBINATION_TARGET_PERCENT, startTime, endTime);
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
