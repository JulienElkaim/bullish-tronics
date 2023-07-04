package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.UserLocationContainsPercentDealDto;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;

public class UserLocationContainsPercentDealDtoAdapter {
    public UserLocationContainsPercentDealDto adapte(UserLocationContainsPercentDeal deal) {
        return new UserLocationContainsPercentDealDto(
                deal.getDealId(),
                deal.getTagline(),
                deal.getStartTime(),
                deal.getEndTime(),
                deal.getPercent(),
                deal.getLocationSubtext()
        );
    }
}
