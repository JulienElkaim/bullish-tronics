package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.ItemCombinationTargetPercentDealDto;
import io.github.bullishtronics.checkout.io.deal.ItemModuloLastPercentDealDto;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;

public class ItemCombinationTargetPercentDealDtoAdapter {
    public ItemCombinationTargetPercentDealDto adapte(ItemCombinationTargetPercentDeal deal) {
        return new ItemCombinationTargetPercentDealDto(
                deal.getDealId(),
                deal.getTargetProductId(),
                deal.getPercent(),
                deal.getTagline(),
                deal.getStartTime(),
                deal.getEndTime(),
                deal.getProductIdsToCombine()
        );
    }
}
