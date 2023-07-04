package io.github.bullishtronics.checkout.core.basket;

import io.github.bullishtronics.checkout.models.basket.Basket;

// Never fails, should be convenient with strange request
public interface BasketStore {
    Basket getBasket(String basketOwnerId);
    Basket add(String productId, Integer quantitiesToAdd, String basketOwnerId);
    Basket remove(String productId, Integer quantitiesToRemove, String basketOwnerId);
    void clear(String basketOwnerId);
}
