package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.DiscounterTemplateTest;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalPercentDiscounterTest extends DiscounterTemplateTest<GlobalDeal> {
    private final GlobalDeal deal = mock(GlobalDeal.class);
    private final Integer percent = 10;
    private final String tagline = "tagline";

    @BeforeEach
    void init() {
        discounter = new GlobalPercentDiscounter(productService);
        when(deal.getPercent()).thenReturn(percent);
        when(deal.getTagline()).thenReturn(tagline);
    }

    @Test
    void computeDeal() {
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount.getProductId());
        assertTrue(BigDecimal.ONE.compareTo(discount.getAmount()) == 0);
        assertEquals(tagline, discount.getDealTagline());
        assertFalse(discount.isStackable());
    }

    @Test
    void computeDeal_ProductServiceDoesNotFindProduct_thenThrow() {
        when(productService.getById(productId)).thenReturn(null);
        DiscountActionException exception = assertThrows(DiscountActionException.class, () -> discounter.computeDeal(basket, deal));
        assertEquals("Can't compute discount, one product does not exist: " + productId, exception.getMessage());
    }

    @Override
    protected Class<GlobalDeal> assignableDealClass() {
        return GlobalDeal.class;
    }

    @Override
    protected DealType getApplicableDealType() {
        return DealType.GLOBAL_PERCENT;
    }
}