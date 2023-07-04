package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.basket.BasketService;
import io.github.bullishtronics.checkout.core.deal.DiscounterService;
import io.github.bullishtronics.checkout.core.purchase.OrderStore;
import io.github.bullishtronics.checkout.core.purchase.PurchaseService;
import io.github.bullishtronics.checkout.core.purchase.QuotationStore;
import io.github.bullishtronics.checkout.core.purchase.exception.OrderActionException;
import io.github.bullishtronics.checkout.core.purchase.exception.QuotationActionException;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PurchaseServiceImplTest {
    private PurchaseService purchaseService;

    private final DiscounterService discounterService = mock(DiscounterService.class);
    private final UserService userService = mock(UserService.class);
    private final QuotationStore quotationStore = mock(QuotationStore.class);
    private final QuotationFactory quotationFactory = mock(QuotationFactory.class);
    private final OrderStore orderStore = mock(OrderStore.class);
    private final OrderFactory orderFactory = mock(OrderFactory.class);
    private final BasketService basketService = mock(BasketService.class);

    private final Quotation quotation = mock(Quotation.class);
    private final Order order = mock(Order.class);
    private final Basket basket = mock(Basket.class);
    private final Discount discount = mock(Discount.class);
    private final Duration defaultDuration = Duration.of(2, ChronoUnit.MINUTES);
    private final User requester = mock(User.class);
    private final User basketOwner = mock(User.class);
    private final String basketOwnerId = "basketOwnerId";
    private final String quotationId = "quotationId";
    private final String orderId = "orderId";

    @BeforeEach
    void init() {
        purchaseService = new PurchaseServiceImpl(discounterService, userService, quotationStore, quotationFactory, orderStore, orderFactory, basketService);

        when(requester.getRole()).thenReturn(Role.ADMIN);
        when(requester.getUsername()).thenReturn(basketOwnerId);

        when(quotationFactory.create(basket, List.of(discount), defaultDuration, basketOwner)).thenReturn(quotation);
        when(basketService.getBasket(basketOwnerId, requester)).thenReturn(basket);
        when(basket.getOwnerId()).thenReturn(basketOwnerId);
        when(userService.getByUsername(basketOwnerId)).thenReturn(basketOwner);
        when(discounterService.compute(basket)).thenReturn(List.of(discount));

        when(orderFactory.generateOrderRecord(quotation)).thenReturn(order);
        when(quotationStore.getQuotationByOwnerId(requester.getUsername())).thenReturn(quotation);
        when(quotation.getQuotationId()).thenReturn(quotationId);
        when(quotation.getEndValidityTime()).thenReturn(Instant.now().plus(defaultDuration));
    }

    @Test
    void quote_thenSuccess() {
        Quotation quote = purchaseService.quote(basketOwnerId, requester);

        assertSame(quotation, quote);
        verify(discounterService).compute(basket);
        verify(quotationStore).save(basketOwnerId, quotation);
    }

    @Test
    void quote_whenRequesterIsNotAdminButBasketOwner_thenSuccess() {
        when(requester.getRole()).thenReturn(Role.CUSTOMER);
        Quotation quote = purchaseService.quote(basketOwnerId, requester);

        assertSame(quotation, quote);
        verify(discounterService).compute(basket);
        verify(quotationStore).save(basketOwnerId, quotation);
    }

    @Test
    void quote_whenRequesterIsNotBasketOwnerButIsAdmin_thenSuccess() {
        when(requester.getUsername()).thenReturn("notBasketOwnerId");
        Quotation quote = purchaseService.quote(basketOwnerId, requester);

        assertSame(quotation, quote);
        verify(discounterService).compute(basket);
        verify(quotationStore).save(basketOwnerId, quotation);
    }

    @Test
    void quote_whenRequesterIsNeitherAdminOrBasketOwner_thenThrowException() {
        when(requester.getUsername()).thenReturn("notBasketOwnerId");
        when(requester.getRole()).thenReturn(Role.CUSTOMER);

        QuotationActionException exception = assertThrows(QuotationActionException.class, () -> purchaseService.quote(basketOwnerId, requester));

        assertEquals("Can't perform quotation as the requester is not admin or the owner of the submitted basket. Owner is: " + basketOwnerId, exception.getMessage());
        verify(quotationStore, never()).save(any(), any());
    }

    @Test
    void quote_discounterThrow_thenThrowException() {
        String errorMessage = "error";
        when(discounterService.compute(basket)).thenThrow(new RuntimeException(errorMessage));
        QuotationActionException exception = assertThrows(QuotationActionException.class, () -> purchaseService.quote(basketOwnerId, requester));

        assertEquals("Error while creating quotation: %s".formatted(errorMessage), exception.getMessage());
        verify(discounterService).compute(basket);
        verify(quotationStore, never()).save(any(), any());
    }

    @Test
    void quote_factoryThrow_thenThrowException() {
        String errorMessage = "error";
        when(quotationFactory.create(any(), any(), any(), any())).thenThrow(new RuntimeException(errorMessage));
        QuotationActionException exception = assertThrows(QuotationActionException.class, () -> purchaseService.quote(basketOwnerId, requester));

        assertEquals("Error while creating quotation: %s".formatted(errorMessage), exception.getMessage());
        verify(discounterService).compute(basket);
        verify(quotationStore, never()).save(any(), any());
    }

    @Test
    void placeOrder_successfully() {
        Order result = purchaseService.placeOrder(quotationId, requester);

        verify(orderStore).save(order);
        assertSame(order, result);
    }

    @Test
    void placeOrder_whenThereIsNoQuotationForThisUser_thenThrowException() {
        when(quotationStore.getQuotationByOwnerId(basketOwnerId)).thenReturn(null);
        OrderActionException exception = assertThrows(OrderActionException.class, () -> purchaseService.placeOrder(quotationId, requester));

        assertEquals("Can't find any quotation for this user.", exception.getMessage());
        verify(orderStore, never()).save(any());
    }

    @Test
    void placeOrder_whenQuotationStoredIsNotTheOneUserWantsToUse_thenThrowException() {
        Quotation anotherQuotation = mock(Quotation.class);
        String anotherQuotationId = "anotherQuotationId";
        when(anotherQuotation.getQuotationId()).thenReturn(anotherQuotationId);
        when(quotationStore.getQuotationByOwnerId(basketOwnerId)).thenReturn(anotherQuotation);
        OrderActionException exception = assertThrows(OrderActionException.class, () -> purchaseService.placeOrder(quotationId, requester));

        assertEquals("Quotation you have provided does not exist or is not available anymore. You provided %s and now available for this user is: %s".formatted(
                quotationId,
                anotherQuotationId), exception.getMessage());
        verify(orderStore, never()).save(any());
    }

    @Test
    void placeOrder_whenQuotationDoesNotHaveEndOfValidity_successfully() {
        when(quotation.getEndValidityTime()).thenReturn(null);
        Order result = purchaseService.placeOrder(quotationId, requester);

        verify(orderStore).save(order);
        assertSame(order, result);
    }

    @Test
    void placeOrder_whenQuotationEndValidityIsPassed_thenThrowException() {
        when(quotation.getEndValidityTime()).thenReturn(Instant.now().minus(defaultDuration));
        OrderActionException exception = assertThrows(OrderActionException.class, () -> purchaseService.placeOrder(quotationId, requester));

        assertEquals("Can't place the order as the quotation is expired: " + quotation.getEndValidityTime(), exception.getMessage());
        verify(orderStore, never()).save(any());
    }

    @Test
    void getMyOrders_userHasOrders() {
        when(orderStore.getByUserId(basketOwnerId)).thenReturn(List.of(order));
        List<Order> result = purchaseService.getMyOrders(requester);
        assertEquals(1, result.size());
        assertSame(order, result.get(0));
    }

    @Test
    void getMyOrders_userDoesNotHaveOrders_thenEmptyList() {
        List<Order> result = purchaseService.getMyOrders(requester);
        assertTrue(result.isEmpty());
    }

    @Test
    void getById() {
        when(orderStore.getById(orderId)).thenReturn(order);
        when(order.getUserId()).thenReturn(basketOwnerId);
        Order result = purchaseService.getById(orderId, requester);

        assertSame(order, result);
    }

    @Test
    void getById_andRequesterIsNotAdminButStillOwnTheOrder_thenSuccess() {
        when(orderStore.getById(orderId)).thenReturn(order);
        when(order.getUserId()).thenReturn(basketOwnerId);
        when(requester.getRole()).thenReturn(Role.CUSTOMER);

        Order result = purchaseService.getById(orderId, requester);

        assertSame(order, result);
    }

    @Test
    void getById_andRequesterDoesNotOwnTheOrderButIsAdmin_thenSuccess() {
        when(orderStore.getById(orderId)).thenReturn(order);
        when(order.getUserId()).thenReturn("AnotherOwnerId");

        Order result = purchaseService.getById(orderId, requester);

        assertSame(order, result);
    }

    @Test
    void getById_andRequesterIsNeitherOwnerOrAdmin_thenSuccess() {
        when(orderStore.getById(orderId)).thenReturn(order);
        when(order.getUserId()).thenReturn("AnotherOwnerId");
        when(requester.getRole()).thenReturn(Role.CUSTOMER);

        OrderActionException exception = assertThrows(OrderActionException.class, () -> purchaseService.getById(orderId, requester));

        assertEquals("Can't get the order as the requester is not admin or the owner of the order: " + orderId, exception.getMessage());
    }
}