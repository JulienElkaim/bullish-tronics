package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.DealDto;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;

public class DealDtoAdapter {
    private final ItemModuloLastPercentDealDtoAdapter imlpDealDtoAdapter;
    private final GlobalDealDtoAdapter globalDealDtoAdapter;
    private final ItemCombinationTargetPercentDealDtoAdapter ictpDealDtoAdapter;
    private final UserLocationContainsPercentDealDtoAdapter ulcpDealDtoAdapter;

    public DealDtoAdapter(ItemModuloLastPercentDealDtoAdapter imlpDealDtoAdapter,
                          GlobalDealDtoAdapter globalDealDtoAdapter,
                          ItemCombinationTargetPercentDealDtoAdapter ictpDealDtoAdapter,
                          UserLocationContainsPercentDealDtoAdapter ulcpDealDtoAdapter) {
        this.imlpDealDtoAdapter = imlpDealDtoAdapter;
        this.globalDealDtoAdapter = globalDealDtoAdapter;
        this.ictpDealDtoAdapter = ictpDealDtoAdapter;
        this.ulcpDealDtoAdapter = ulcpDealDtoAdapter;
    }

    public DealDto adapte(Deal deal) {
        return switch (deal.getType()) {
            case GLOBAL_PERCENT -> globalDealDtoAdapter.adapte((GlobalDeal) deal);
            case ITEM_MODULO_LAST_ITEM_PERCENT -> imlpDealDtoAdapter.adapte((ItemModuloLastPercentDeal) deal);
            case ITEM_COMBINATION_TARGET_PERCENT -> ictpDealDtoAdapter.adapte((ItemCombinationTargetPercentDeal) deal);
            case USER_LOCATION_CONTAINS_PERCENT -> ulcpDealDtoAdapter.adapte((UserLocationContainsPercentDeal) deal);
        };
    }
}
