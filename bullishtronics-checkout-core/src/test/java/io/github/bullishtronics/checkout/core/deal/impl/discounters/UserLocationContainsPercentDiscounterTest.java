package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.DiscounterTemplateTest;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserLocationContainsPercentDiscounterTest extends DiscounterTemplateTest<UserLocationContainsPercentDeal> {
    private final UserLocationContainsPercentDeal deal = mock(UserLocationContainsPercentDeal.class);
    private final UserService userService = mock(UserService.class);
    private final Integer percent = 10;
    private final String tagline = "tagline";
    private final String locationSubText = "locationSubText";
    private final User user = mock(User.class);
    private final String basketOwnerId = "basketOwnerId";

    @BeforeEach
    void init() {
        discounter = new UserLocationContainsPercentDiscounter(productService, userService);
        when(deal.getPercent()).thenReturn(percent);
        when(deal.getTagline()).thenReturn(tagline);
        when(deal.getLocationSubtext()).thenReturn(locationSubText);
        when(userService.getByUsername(basketOwnerId)).thenReturn(user);
        when(basket.getOwnerId()).thenReturn(basketOwnerId);
        when(user.getAddress()).thenReturn("something here" + locationSubText + "and there");
    }

    @Test
    void computeDeal() {
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount.getProductId());
        assertEquals(0, BigDecimal.ONE.compareTo(discount.getAmount()));
        assertEquals(tagline, discount.getDealTagline());
        assertFalse(discount.isStackable());
    }

    @Test
    void computeDeal_whenPercentIsNegative_thenReturnNull() {
        when(deal.getPercent()).thenReturn(-13);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenPercentIsUnder1_thenReturnNull() {
        when(deal.getPercent()).thenReturn(0);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenLocationSubtextIsNull_thenReturnNull() {
        when(deal.getLocationSubtext()).thenReturn(null);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenLocationSubtextIsBlank_thenReturnNull() {
        when(deal.getLocationSubtext()).thenReturn("   ");
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenUserServiceDoesNotFindBasketOwner_thenReturnNull() {
        when(userService.getByUsername(basketOwnerId)).thenReturn(null);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenBasketOwnerIsHomeless_thenReturnNull() {
        when(user.getAddress()).thenReturn(null);
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_whenBasketOwnerAddressDoesNotMatchTheRequirements_thenReturnNull() {
        when(user.getAddress()).thenReturn("Completely different address");
        Discount discount = discounter.computeDeal(basket, deal);
        assertNull(discount);
    }

    @Test
    void computeDeal_ProductServiceDoesNotFindProduct_thenThrow() {
        when(productService.getById(productId)).thenReturn(null);
        DiscountActionException exception = assertThrows(DiscountActionException.class, () -> discounter.computeDeal(basket, deal));
        assertEquals("Can't compute discount, one product does not exist: " + productId, exception.getMessage());
    }

    @Override
    protected Class<UserLocationContainsPercentDeal> assignableDealClass() {
        return UserLocationContainsPercentDeal.class;
    }

    @Override
    protected DealType getApplicableDealType() {
        return DealType.USER_LOCATION_CONTAINS_PERCENT;
    }
}