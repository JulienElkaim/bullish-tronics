package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.ItemModuloLastPercentDealDto;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;

public class ItemModuloLastPercentDealDtoAdapter {
    public ItemModuloLastPercentDealDto adapte(ItemModuloLastPercentDeal deal) {
        return new ItemModuloLastPercentDealDto(
                deal.getDealId(),
                deal.getTagline(),
                deal.getStartTime(),
                deal.getEndTime(),
                deal.getProductId(),
                deal.getModuloNumber(),
                deal.getPercent()
        );
    }
}
