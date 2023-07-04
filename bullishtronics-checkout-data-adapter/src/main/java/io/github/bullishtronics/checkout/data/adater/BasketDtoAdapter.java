package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.basket.BasketDto;
import io.github.bullishtronics.checkout.models.basket.Basket;

public class BasketDtoAdapter {

    public BasketDto adapte(Basket basket) {
        return new BasketDto(
                basket.getOwnerId(),
                basket.getProductsByQuantity()
        );
    }
}
