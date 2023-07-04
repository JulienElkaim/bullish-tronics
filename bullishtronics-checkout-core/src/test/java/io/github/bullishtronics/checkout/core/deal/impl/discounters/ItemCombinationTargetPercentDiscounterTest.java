package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.DiscounterTemplateTest;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemCombinationTargetPercentDiscounterTest extends DiscounterTemplateTest<ItemCombinationTargetPercentDeal> {
    private final ItemCombinationTargetPercentDeal deal = mock(ItemCombinationTargetPercentDeal.class);
    private final Integer percent = 10;
    private final String tagline = "tagline";
    private final String secondProductId = "secondProductId";
    private final Product secondProduct = mock(Product.class);
    private final BigDecimal secondProductPrice = BigDecimal.ONE;

    @BeforeEach
    void init() {
        discounter = new ItemCombinationTargetPercentDiscounter(productService);
        when(productService.getById(secondProductId)).thenReturn(secondProduct);
        when(secondProduct.getPrice()).thenReturn(secondProductPrice);
        when(deal.getPercent()).thenReturn(percent);
        when(deal.getTagline()).thenReturn(tagline);
        when(deal.getTargetProductId()).thenReturn(productId);
        when(deal.getProductIdsToCombine()).thenReturn(Set.of(productId, secondProductId));
        when(basket.getProductsByQuantity()).thenReturn(
                Map.of(productId, productInitialQuantity, secondProductId, 3));
    }

    @Test
    void computeDeal() {
        Discount discount = discounter.computeDeal(basket, deal);
        assertEquals(productId, discount.getProductId());
        assertTrue(BigDecimal.ONE.compareTo(discount.getAmount()) == 0);
        assertEquals(tagline, discount.getDealTagline());
        assertTrue(discount.isStackable());
    }

    @Test
    void computeDeal_ProductServiceDoesNotFindProduct_thenThrow() {
        when(productService.getById(productId)).thenReturn(null);
        DiscountActionException exception = assertThrows(DiscountActionException.class, () -> discounter.computeDeal(basket, deal));
        assertEquals("Can't compute discount, one product does not exist: " + productId, exception.getMessage());
    }

    @Test
    void computeDeal_whenTargetProductIdIsNull_thenReturnNull() {
        when(deal.getTargetProductId()).thenReturn(null);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenProductIdsToCombineIsNull_thenReturnNull() {
        when(deal.getProductIdsToCombine()).thenReturn(null);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenProductIdsToCombineHasOnly1Element_thenReturnNull() {
        when(deal.getProductIdsToCombine()).thenReturn(Set.of(secondProductId));
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenProductIdsToCombineDoesNotContainsTargetProductId_thenReturnNull() {
        when(deal.getProductIdsToCombine()).thenReturn(Set.of(secondProductId, "anotherProductUnexpected"));
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenBasketDoesNotHaveAllTheRequiredProducts_thenReturnNull() {
        when(basket.getProductsByQuantity()).thenReturn(Map.of(productId, productInitialQuantity));
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Override
    protected Class<ItemCombinationTargetPercentDeal> assignableDealClass() {
        return ItemCombinationTargetPercentDeal.class;
    }

    @Override
    protected DealType getApplicableDealType() {
        return DealType.ITEM_COMBINATION_TARGET_PERCENT;
    }
}