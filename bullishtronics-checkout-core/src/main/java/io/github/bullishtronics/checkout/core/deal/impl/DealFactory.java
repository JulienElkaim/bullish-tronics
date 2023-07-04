package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.exception.DealNotSupportedException;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDealDetails;

import java.util.UUID;

public class DealFactory {

    public Deal build(DealDetails dealDetails) throws DealNotSupportedException {
        if (dealDetails instanceof GlobalDealDetails dealDetailsTyped) {
            return new GlobalDeal(
                    generateId(),
                    dealDetailsTyped.getTagline(),
                    dealDetailsTyped.getStartTime(),
                    dealDetailsTyped.getEndTime(),
                    dealDetailsTyped.getPercent()
            );
        } else if (dealDetails instanceof ItemModuloLastPercentDealDetails dealDetailsTyped) {
            return new ItemModuloLastPercentDeal(
                    generateId(),
                    dealDetailsTyped.getProductId(),
                    dealDetailsTyped.getModuloNumber(),
                    dealDetailsTyped.getPercent(),
                    dealDetailsTyped.getTagline(),
                    dealDetailsTyped.getStartTime(),
                    dealDetailsTyped.getEndTime()
            );
        } else if (dealDetails instanceof ItemCombinationTargetPercentDealDetails dealDetailsTyped) {
            return new ItemCombinationTargetPercentDeal(
                    generateId(),
                    dealDetailsTyped.getTargetProductId(),
                    dealDetailsTyped.getPercent(),
                    dealDetailsTyped.getTagline(),
                    dealDetailsTyped.getStartTime(),
                    dealDetailsTyped.getEndTime(),
                    dealDetailsTyped.getProductIdsToCombine()
            );
        } else if (dealDetails instanceof UserLocationContainsPercentDealDetails dealDetailsTyped) {
            return new UserLocationContainsPercentDeal(
                    generateId(),
                    dealDetailsTyped.getTagline(),
                    dealDetailsTyped.getStartTime(),
                    dealDetailsTyped.getEndTime(),
                    dealDetailsTyped.getPercent(),
                    dealDetailsTyped.getLocationSubtext()
            );
        }

        throw new DealNotSupportedException("We do not support this DealDetails yet: " + dealDetails.getClass().getName());
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
