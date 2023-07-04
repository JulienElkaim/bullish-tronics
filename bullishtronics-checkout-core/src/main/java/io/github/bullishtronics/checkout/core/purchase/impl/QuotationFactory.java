package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.order.QuotationDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class QuotationFactory {
    Quotation create(Basket basket, List<Discount> discounts, Duration quotationValidityDuration, User basketOwner) {
        return new Quotation(
                UUID.randomUUID().toString(),
                Instant.now().plus(quotationValidityDuration),
                basketOwner.getUsername(),
                new QuotationDetails(basket.getProductsByQuantity(), discounts)
        );
    }
}
