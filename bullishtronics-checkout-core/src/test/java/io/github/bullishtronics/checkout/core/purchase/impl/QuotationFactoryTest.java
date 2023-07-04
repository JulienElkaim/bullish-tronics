package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuotationFactoryTest {
    private QuotationFactory quotationFactory;
    private final Basket basket = mock(Basket.class);
    private final Discount discount = mock(Discount.class);
    private final Discount discount2 = mock(Discount.class);
    private final User basketOwner = mock(User.class);
    private final String basketOwnerId = "basketOwnerId";
    private final String productId = "productId";
    private final Duration quotationValidityDuration = Duration.ofDays(7);

    @BeforeEach
    void init() {
        quotationFactory = new QuotationFactory();
        when(basket.getProductsByQuantity()).thenReturn(Map.of(productId, 2));
        when(basketOwner.getUsername()).thenReturn(basketOwnerId);
    }

    @Test
    void create_useTheInputsAsExpected() {
        Instant before = Instant.now();
        Quotation result = quotationFactory.create(basket, List.of(discount, discount2), quotationValidityDuration, basketOwner);
        Instant after = Instant.now();

        assertEquals(basketOwnerId, result.getBasketOwnerId());
        assertTrue(before.plus(quotationValidityDuration).isBefore(result.getEndValidityTime()));
        assertTrue(after.plus(quotationValidityDuration).isAfter(result.getEndValidityTime()));
        assertEquals(Map.of(productId, 2), result.getQuotationDetails().getProductsByQuantity());
        assertEquals(List.of(discount, discount2), result.getQuotationDetails().getDiscounts());
        assertNotNull(result.getQuotationId());
    }

}