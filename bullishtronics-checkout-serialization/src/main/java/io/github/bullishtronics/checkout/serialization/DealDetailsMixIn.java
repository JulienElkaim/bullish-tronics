package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDealDetails;


@JsonTypeInfo(
        property = "dealType",
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GlobalDealDetails.class, name = "GLOBAL_PERCENT"),
        @JsonSubTypes.Type(value = ItemModuloLastPercentDealDetails.class, name = "ITEM_MODULO_LAST_ITEM_PERCENT"),
        @JsonSubTypes.Type(value = ItemCombinationTargetPercentDealDetails.class, name = "ITEM_COMBINATION_TARGET_PERCENT"),
        @JsonSubTypes.Type(value = UserLocationContainsPercentDealDetails.class, name = "USER_LOCATION_CONTAINS_PERCENT"),
})
public class DealDetailsMixIn {

}
