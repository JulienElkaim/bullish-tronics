package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;
import io.github.bullishtronics.checkout.models.deal.Discount;

public class DiscountDtoAdapter {
    public DiscountDto adapte(Discount discount) {
        return new DiscountDto(
                discount.getProductId(),
                discount.getAmount(),
                discount.getDealTagline(),
                discount.isStackable()
        );
    }
}
