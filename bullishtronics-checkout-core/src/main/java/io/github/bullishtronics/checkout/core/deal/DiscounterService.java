package io.github.bullishtronics.checkout.core.deal;

import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Discount;

import java.util.List;

/**
 * Knows all the available discounter,
 * Use the DealService to apply deals based on criteria
 */
public interface DiscounterService {
    List<Discount> compute(Basket basket);

}