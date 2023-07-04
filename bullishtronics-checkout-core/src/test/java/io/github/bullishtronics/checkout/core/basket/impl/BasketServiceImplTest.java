package io.github.bullishtronics.checkout.core.basket.impl;

import io.github.bullishtronics.checkout.core.basket.BasketStore;
import io.github.bullishtronics.checkout.core.basket.exception.BasketActionException;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.bullishtronics.checkout.models.user.Role.ADMIN;
import static io.github.bullishtronics.checkout.models.user.Role.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BasketServiceImplTest {
    private final BasketStore basketStore = mock(BasketStore.class);
    private final String productId = "productId";
    private final String basketOwnerId = "basketOwnerId";
    private final Integer quantity = 2;
    private final Integer quantityZero = 0;
    private final Integer quantityNegative = -1;
    private final User requester = mock(User.class);
    private final Basket basket = mock(Basket.class);

    private BasketServiceImpl basketService;

    @BeforeEach
    void init() {
        basketService = new BasketServiceImpl(basketStore);

        when(requester.getRole()).thenReturn(ADMIN);
        when(requester.getUsername()).thenReturn(basketOwnerId);
        when(basketStore.remove(productId, quantity, basketOwnerId)).thenReturn(basket);


        when(basketStore.add(productId, quantity, basketOwnerId)).thenReturn(basket);
        when(basketStore.getBasket(basketOwnerId)).thenReturn(basket);
    }

    @Test
    void removeProduct() {
        Basket result = basketService.removeProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).remove(productId, quantity, basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void removeProduct_whenItLeadsToTheBasketDeletion() {
        when(basketStore.remove(productId, quantity, basketOwnerId)).thenReturn(null);
        Basket result = basketService.removeProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).remove(productId, quantity, basketOwnerId);
        assertEquals(basketOwnerId, result.getOwnerId());
        assertTrue(result.getProductsByQuantity().isEmpty());
    }

    @Test
    void removeProduct_whenQuantityZero() {
        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.removeProduct(productId, quantityZero, basketOwnerId, requester));
        verify(basketStore, never()).remove(any(), any(), any());
        assertEquals("Quantity must be greater than 0.", exception.getMessage());
    }

    @Test
    void removeProduct_whenQuantityIsNegative() {
        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.removeProduct(productId, quantityNegative, basketOwnerId, requester));
        verify(basketStore, never()).remove(any(), any(), any());
        assertEquals("Quantity must be greater than 0.", exception.getMessage());
    }

    @Test
    void removeProduct_whenNotOwnerButIsAdmin() {
        when(requester.getUsername()).thenReturn("notTheSamePerson");
        Basket result = basketService.removeProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).remove(productId, quantity, basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void removeProduct_whenNotAdminButIsOwner() {
        when(requester.getRole()).thenReturn(CUSTOMER);
        Basket result = basketService.removeProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).remove(productId, quantity, basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void removeProduct_whenNeitherOwnerOrAdmin() {
        when(requester.getRole()).thenReturn(CUSTOMER);
        when(requester.getUsername()).thenReturn("notTheSamePerson");

        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.removeProduct(productId, quantity, basketOwnerId, requester));
        verify(basketStore, never()).remove(any(), any(), any());
        assertEquals("Only basket owner or admin can remove products from basket.", exception.getMessage());
    }

    @Test
    void addProduct() {
        Basket result = basketService.addProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).add(productId, quantity, basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void addProduct_whenQuantityZero() {
        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.addProduct(productId, quantityZero, basketOwnerId, requester));
        verify(basketStore, never()).add(any(), any(), any());
        assertEquals("Quantity must be greater than 0.", exception.getMessage());
    }

    @Test
    void addProduct_whenQuantityIsNegative() {
        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.addProduct(productId, quantityNegative, basketOwnerId, requester));
        verify(basketStore, never()).add(any(), any(), any());
        assertEquals("Quantity must be greater than 0.", exception.getMessage());
    }

    @Test
    void addProduct_whenNotOwnerButIsAdmin() {
        when(requester.getUsername()).thenReturn("notTheSamePerson");
        Basket result = basketService.addProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).add(productId, quantity, basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void addProduct_whenNotAdminButIsOwner() {
        when(requester.getRole()).thenReturn(CUSTOMER);
        Basket result = basketService.addProduct(productId, quantity, basketOwnerId, requester);
        verify(basketStore).add(productId, quantity, basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void addProduct_whenNeitherOwnerOrAdmin() {
        when(requester.getRole()).thenReturn(CUSTOMER);
        when(requester.getUsername()).thenReturn("notTheSamePerson");

        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.addProduct(productId, quantity, basketOwnerId, requester));
        verify(basketStore, never()).add(any(), any(), any());
        assertEquals("Only basket owner or admin can add products to basket.", exception.getMessage());
    }

    @Test
    void clear() {
        basketService.clear(basketOwnerId, requester);
        verify(basketStore).clear(basketOwnerId);
    }

    @Test
    void clear_whenNotOwnerButStillAdmin() {
        when(requester.getUsername()).thenReturn("notTheSamePerson");
        basketService.clear(basketOwnerId, requester);
        verify(basketStore).clear(basketOwnerId);
    }

    @Test
    void clear_whenNotAdminButStillOwner() {
        when(requester.getRole()).thenReturn(CUSTOMER);
        basketService.clear(basketOwnerId, requester);
        verify(basketStore).clear(basketOwnerId);
    }

    @Test
    void clear_whenRequesterIsNeitherAdminOrOwner() {
        when(requester.getRole()).thenReturn(CUSTOMER);
        when(requester.getUsername()).thenReturn("notTheSamePerson");
        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.clear(basketOwnerId, requester));
        assertEquals("Only basket owner or admin can clear basket.", exception.getMessage());
    }

    @Test
    void getBasket(){
        Basket result = basketService.getBasket(basketOwnerId, requester);
        verify(basketStore).getBasket(basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void getBasket_whenNotAdminButStillOwner_thenThrow(){
        when(requester.getRole()).thenReturn(CUSTOMER);
        Basket result = basketService.getBasket(basketOwnerId, requester);
        verify(basketStore).getBasket(basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void getBasket_whenNotOwnerButStillAdmin_thenThrow(){
        when(requester.getUsername()).thenReturn("notTheSamePerson");
        Basket result = basketService.getBasket(basketOwnerId, requester);
        verify(basketStore).getBasket(basketOwnerId);
        assertSame(basket, result);
    }

    @Test
    void getBasket_whenNeitherOwnerOrAdmin_thenThrow(){
        when(requester.getRole()).thenReturn(CUSTOMER);
        when(requester.getUsername()).thenReturn("notTheSamePerson");

        BasketActionException exception = assertThrows(BasketActionException.class, () -> basketService.getBasket(basketOwnerId, requester));
        assertEquals("Only basket owner or admin can get basket.", exception.getMessage());
    }
}