package io.github.bullishtronics.checkout.serialization;


import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDealDetails;
import io.github.bullishtronics.checkout.models.product.CreateProductDetails;
import io.github.bullishtronics.checkout.models.user.CreateUserDetails;

import java.util.HashMap;
import java.util.Map;

public class MixinsConfig {

    private static final Map<Class<?>, Class<?>> DEALS_MIXINS = Map.of(
            DealDetails.class, DealDetailsMixIn.class,
            GlobalDealDetails.class, GlobalDealDetailsMixIn.class,
            ItemModuloLastPercentDealDetails.class, ItemModuloLastPercentDealDetailsMixIn.class,
            ItemCombinationTargetPercentDealDetails.class, ItemCombinationTargetPercentDealDetailsMixIn.class,
            UserLocationContainsPercentDealDetails.class, UserLocationContainsPercentDealDetailsMixIn.class
    );

    private static final Map<Class<?>, Class<?>> OTHER_MIXINS = Map.of(
            CreateProductDetails.class, CreateProductDetailsMixIn.class,
            CreateUserDetails.class, CreateUserDetailsMixIn.class
    );
    public static final Map<Class<?>, Class<?>> JACKSON_MIXINS = new HashMap<>() {{
        putAll(DEALS_MIXINS);
        putAll(OTHER_MIXINS);
    }};
}
