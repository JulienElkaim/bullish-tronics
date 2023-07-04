package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.DiscounterTemplateTest;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemModuloLastPercentDiscounterTest extends DiscounterTemplateTest<ItemModuloLastPercentDeal> {
    private final ItemModuloLastPercentDeal deal = mock(ItemModuloLastPercentDeal.class);
    private final Integer percent = 10;
    private final String tagline = "tagline";
    private final Integer moduloNumber = 3;

    @BeforeEach
    void init() {
        discounter = new ItemModuloLastPercentDiscounter(productService);
        when(deal.getPercent()).thenReturn(percent);
        when(deal.getTagline()).thenReturn(tagline);
        when(deal.getProductId()).thenReturn(productId);
        when(deal.getModuloNumber()).thenReturn(moduloNumber);
        when(basket.getProductsByQuantity()).thenReturn(Map.of(productId,7));
    }

    @Test
    void computeDeal() {
        Discount discount = discounter.computeDeal(basket, deal);
        assertEquals(productId, discount.getProductId());
        assertTrue(BigDecimal.valueOf(2).compareTo(discount.getAmount()) == 0);
        assertEquals(tagline, discount.getDealTagline());
        assertTrue(discount.isStackable());
    }

    @Test
    void computeDeal_whenPercentIsNegative_thenReturnNull(){
        when(deal.getPercent()).thenReturn(-percent);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenPercentIsZero_thenReturnNull(){
        when(deal.getPercent()).thenReturn(0);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenModuloNumberIsNegative_thenReturnNull(){
        when(deal.getModuloNumber()).thenReturn(-moduloNumber);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenModuloNumberIsZero_thenReturnNull(){
        when(deal.getModuloNumber()).thenReturn(0);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenDealProductIdIsNull_thenReturnNull(){
        when(deal.getProductId()).thenReturn(null);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenBasketDoesNotContainTheProduct_thenReturnNull(){
        when(deal.getProductId()).thenReturn("anotherProductId");
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenTheActualQuantityInBasketIsLessThanModuloNumber_thenReturnNull(){
        when(basket.getProductsByQuantity()).thenReturn(Map.of(productId,2));
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_ProductServiceDoesNotFindProduct_thenThrow() {
        when(productService.getById(productId)).thenReturn(null);
        DiscountActionException exception = assertThrows(DiscountActionException.class, () -> discounter.computeDeal(basket, deal));
        assertEquals("Can't compute discount, product does not exist: " + productId, exception.getMessage());
    }

    @Override
    protected Class<ItemModuloLastPercentDeal> assignableDealClass() {
        return ItemModuloLastPercentDeal.class;
    }

    @Override
    protected DealType getApplicableDealType() {
        return DealType.ITEM_MODULO_LAST_ITEM_PERCENT;
    }
}