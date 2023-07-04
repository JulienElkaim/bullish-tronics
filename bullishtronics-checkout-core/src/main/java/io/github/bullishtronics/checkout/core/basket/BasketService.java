package io.github.bullishtronics.checkout.core.basket;

import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.basket.Basket;

/**
 * CRUD on Basket objects.
 * Basket owner OR admin can CRUD on Basket objects.
 */
public interface BasketService {
    Basket removeProduct(String productId, Integer quantity, String basketOwnerId, User requester);

    Basket addProduct(String productId, Integer quantity, String basketOwnerId, User requester);

    void clear(String basketOwnerId, User requester);

    Basket getBasket(String basketOwnerId, User requester);
}
