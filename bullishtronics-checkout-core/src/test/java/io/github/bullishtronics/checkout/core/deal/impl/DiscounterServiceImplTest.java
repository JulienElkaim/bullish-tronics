package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.DealService;
import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountNotExistException;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.github.bullishtronics.checkout.models.deal.DealType.GLOBAL_PERCENT;
import static io.github.bullishtronics.checkout.models.deal.DealType.ITEM_MODULO_LAST_ITEM_PERCENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiscounterServiceImplTest {
    private final DealService dealService = mock(DealService.class);
    private final Basket basket = mock(Basket.class);
    private final Discounter<?> globalDiscounter = mock(Discounter.class);
    private final Discounter<?> itemModuloLastDiscounter = mock(Discounter.class);
    private final Map<DealType, Discounter<?>> discountersMap = Map.of(
            GLOBAL_PERCENT, globalDiscounter,
            ITEM_MODULO_LAST_ITEM_PERCENT, itemModuloLastDiscounter
    );
    private final GlobalDeal globalDeal = mock(GlobalDeal.class);
    private final ItemModuloLastPercentDeal imlpDeal = mock(ItemModuloLastPercentDeal.class);
    private final Discount globalDiscount = mock(Discount.class);
    private final Discount imlpDiscount = mock(Discount.class);

    private DiscounterServiceImpl discounterService;

    @BeforeEach
    void init() {
        discounterService = new DiscounterServiceImpl(dealService, discountersMap);
        when(dealService.getAllActive()).thenReturn(List.of(globalDeal, imlpDeal));
        when(globalDeal.getType()).thenReturn(GLOBAL_PERCENT);
        when(imlpDeal.getType()).thenReturn(ITEM_MODULO_LAST_ITEM_PERCENT);

        when(globalDiscounter.computeDeal(basket, globalDeal)).thenReturn(globalDiscount);
        when(itemModuloLastDiscounter.computeDeal(basket, imlpDeal)).thenReturn(imlpDiscount);

        when(globalDiscount.getAmount()).thenReturn(BigDecimal.TEN);
        when(imlpDiscount.getAmount()).thenReturn(BigDecimal.ONE);
        when(globalDiscount.isStackable()).thenReturn(false);
        when(imlpDiscount.isStackable()).thenReturn(true);
    }

    @Test
    void compute(){
        List<Discount> result = discounterService.compute(basket);
        assertEquals(1, result.size());
        assertSame(globalDiscount, result.get(0));
        verify(globalDiscounter).computeDeal(basket, globalDeal);
        verify(itemModuloLastDiscounter).computeDeal(basket, imlpDeal);
        verify(dealService).getAllActive();
    }

    @Test
    void compute_whenNoActiveDeal_thenNoDiscount(){
        when(dealService.getAllActive()).thenReturn(List.of());
        List<Discount> result = discounterService.compute(basket);

        assertTrue(result.isEmpty());
    }

    @Test
    void compute_whenNoDiscounterForDealType_thenThrow(){
        Deal deal = mock(Deal.class);
        when(deal.getType()).thenReturn(DealType.ITEM_COMBINATION_TARGET_PERCENT);
        when(dealService.getAllActive()).thenReturn(List.of(deal));

        DiscountNotExistException exception = assertThrows(DiscountNotExistException.class, () -> discounterService.compute(basket));
        assertEquals("No discounter for deal type ITEM_COMBINATION_TARGET_PERCENT", exception.getMessage());
    }

    @Test
    void compute_whenDiscounterThrows_thenThrow(){
        String errorMessage = "test";
        when(globalDiscounter.computeDeal(any(),any())).thenThrow(new RuntimeException(errorMessage));

        DiscountActionException exception = assertThrows(DiscountActionException.class, () -> discounterService.compute(basket));
        assertEquals("Can't compute discount for deal %s for the following reason: %s".formatted(globalDeal.getDealId(), errorMessage), exception.getMessage());
    }

}