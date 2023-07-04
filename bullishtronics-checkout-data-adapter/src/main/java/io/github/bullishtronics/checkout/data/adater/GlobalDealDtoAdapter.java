package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.GlobalDealDto;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;

public class GlobalDealDtoAdapter {
    public GlobalDealDto adapte(GlobalDeal deal) {
        return new GlobalDealDto(
                deal.getDealId(),
                deal.getTagline(),
                deal.getStartTime(),
                deal.getEndTime(),
                deal.getPercent()
        );
    }
}
