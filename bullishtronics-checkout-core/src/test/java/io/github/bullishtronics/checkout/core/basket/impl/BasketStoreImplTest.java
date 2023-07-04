package io.github.bullishtronics.checkout.core.basket.impl;

import io.github.bullishtronics.checkout.models.basket.Basket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasketStoreImplTest {
    private final String basketOwnerId = "basketOwnerId";
    private final String productId = "productId";
    private final Integer quantities = 2;
    private BasketStoreImpl basketStore;

    @BeforeEach
    void init() {
        reInit(Map.of());
    }

    @Test
    void add_whenBasketDidNotExist_createANewOne() {
        Basket basketBefore = basketStore.getBasket(basketOwnerId);
        assertEmptyBasket(basketBefore);

        Basket basketAfter = basketStore.add(productId, quantities, basketOwnerId);

        assertEquals(basketOwnerId, basketAfter.getOwnerId());
        Map<String, Integer> productsByQuantity = basketAfter.getProductsByQuantity();
        assertEquals(1, productsByQuantity.size());
        assertTrue(productsByQuantity.containsKey(productId));
        assertEquals(quantities, productsByQuantity.get(productId));
    }

    @Test
    void add_whenBasketExisted_butDidNotHadThisProduct() {
        reInit(Map.of(basketOwnerId, new Basket(basketOwnerId, Map.of("anotherProduct", 3))));
        Basket basketBefore = basketStore.getBasket(basketOwnerId);
        assertFalse(basketBefore.getProductsByQuantity().containsKey(productId));

        Basket basketAfter = basketStore.add(productId, quantities, basketOwnerId);
        assertTrue(basketAfter.getProductsByQuantity().containsKey(productId));

        assertEquals(quantities, basketAfter.getProductsByQuantity().get(productId));
    }

    @Test
    void add_whenBasketExisted_andHadQuantitiesOnThisProduct() {
        int initialQuantities = 3;
        reInit(Map.of(basketOwnerId, new Basket(basketOwnerId, Map.of(productId, initialQuantities))));
        Basket basketBefore = basketStore.getBasket(basketOwnerId);
        assertTrue(basketBefore.getProductsByQuantity().containsKey(productId));

        Basket basketAfter = basketStore.add(productId, quantities, basketOwnerId);
        assertTrue(basketAfter.getProductsByQuantity().containsKey(productId));

        assertEquals(quantities + initialQuantities, basketAfter.getProductsByQuantity().get(productId));
    }

    @Test
    void remove_whenBasketDidNotExist_NoImpact() {
        Basket before = basketStore.getBasket(basketOwnerId);
        assertEmptyBasket(before);

        Basket result = basketStore.remove(productId, quantities, basketOwnerId);
        assertNull(result);

        Basket after = basketStore.getBasket(basketOwnerId);
        assertEmptyBasket(after);
    }

    @Test
    void remove_whenExistButNoQtyForThisProduct_NoImpact() {
        Basket initialBasket = new Basket(basketOwnerId, Map.of("anotherProduct", 3));
        reInit(Map.of(basketOwnerId, initialBasket));

        Basket result = basketStore.remove(productId, quantities, basketOwnerId);
        assertEquals(initialBasket.getProductsByQuantity().size(), result.getProductsByQuantity().size());
        assertFalse(result.getProductsByQuantity().containsKey(productId));
    }

    @Test
    void remove_whenExistAndThereIsMoreQuantitiesOfThisProduct_reduceQuantities() {
        int initialQty = 10;
        Basket initialBasket = new Basket(basketOwnerId, Map.of(productId, initialQty));
        reInit(Map.of(basketOwnerId, initialBasket));

        Basket result = basketStore.remove(productId, quantities, basketOwnerId);
        assertEquals(1, result.getProductsByQuantity().size());
        assertTrue(result.getProductsByQuantity().containsKey(productId));
        assertEquals(initialQty - quantities, result.getProductsByQuantity().get(productId));
    }

    @Test
    void remove_whenExistAndThereIsLessQuantitiesOfThisProduct_andThereIsAnotherProduct_removeProduct() {
        int initialQty = 1;
        Basket initialBasket = new Basket(basketOwnerId,
                Map.of(productId, initialQty, "anotherProduct", 3));
        reInit(Map.of(basketOwnerId, initialBasket));

        Basket result = basketStore.remove(productId, quantities, basketOwnerId);
        assertEquals(1, result.getProductsByQuantity().size());
        assertFalse(result.getProductsByQuantity().containsKey(productId));
    }

    @Test
    void remove_whenExistAndThereIsLessQuantitiesOfThisProduct_removeBasket() {
        int initialQty = 1;
        Basket initialBasket = new Basket(basketOwnerId,
                Map.of(productId, initialQty));
        reInit(Map.of(basketOwnerId, initialBasket));

        Basket result = basketStore.remove(productId, quantities, basketOwnerId);
        assertNull(result);
    }

    @Test
    void getBasket_whenExist() {
        Basket basketStored = new Basket(basketOwnerId, Map.of());
        reInit(Map.of(basketOwnerId, basketStored));

        Basket result = basketStore.getBasket(basketOwnerId);
        assertSame(basketStored, result);
    }

    @Test
    void getBasket_whenDoesNotExist_giveEmptyPlaceholder() {
        Basket basket = basketStore.getBasket(basketOwnerId);
        assertEquals(basketOwnerId, basket.getOwnerId());
        assertEmptyBasket(basket);
    }

    @Test
    void clear_whenBasketInCache() {
        Basket basketStored = new Basket(basketOwnerId, Map.of(productId, 2));
        reInit(Map.of(basketOwnerId, basketStored));
        Basket basketBefore = basketStore.getBasket(basketOwnerId);
        assertSame(basketStored, basketBefore);

        basketStore.clear(basketOwnerId);

        Basket basketAfter = basketStore.getBasket(basketOwnerId);
        assertEmptyBasket(basketAfter);
        assertNotSame(basketStored, basketAfter);
    }

    @Test
    void clear_whenNoBasketInCache_nothingHappen() {
        basketStore.clear(basketOwnerId);
    }

    private void reInit(Map<String, Basket> basketOwnerId) {
        basketStore = new BasketStoreImpl(basketOwnerId);
    }

    private void assertEmptyBasket(Basket basketBefore) {
        assertTrue(basketBefore.getProductsByQuantity().isEmpty());
    }
}