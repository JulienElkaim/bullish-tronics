package io.github.bullishtronics.checkout.core.basket.impl;

import io.github.bullishtronics.checkout.core.basket.BasketService;
import io.github.bullishtronics.checkout.core.basket.BasketStore;
import io.github.bullishtronics.checkout.core.basket.exception.BasketActionException;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.user.User;

import static io.github.bullishtronics.checkout.models.user.Role.ADMIN;

public class BasketServiceImpl implements BasketService {
    private final BasketStore basketStore;

    public BasketServiceImpl(BasketStore basketStore) {
        this.basketStore = basketStore;
    }

    @Override
    public Basket removeProduct(String productId, Integer quantity, String basketOwnerId, User requester) {
        if (!requesterIsOwnerOrAdmin(requester, basketOwnerId)) {
            throw new BasketActionException("Only basket owner or admin can remove products from basket.");
        } else if (quantity < 1) {
            throw new BasketActionException("Quantity must be greater than 0.");
        }
        Basket updatedBasket = this.basketStore.remove(productId, quantity, basketOwnerId);
        return updatedBasket == null ?
                Basket.empty(basketOwnerId) :
                updatedBasket;
    }

    @Override
    public Basket addProduct(String productId, Integer quantity, String basketOwnerId, User requester) {
        if (!requesterIsOwnerOrAdmin(requester, basketOwnerId)) {
            throw new BasketActionException("Only basket owner or admin can add products to basket.");
        } else if (quantity < 1) {
            throw new BasketActionException("Quantity must be greater than 0.");
        }
        return this.basketStore.add(productId, quantity, basketOwnerId);

    }

    @Override
    public void clear(String basketOwnerId, User requester) {
        if (requesterIsOwnerOrAdmin(requester, basketOwnerId)) {
            this.basketStore.clear(basketOwnerId);
        }else{
            throw new BasketActionException("Only basket owner or admin can clear basket.");
        }
    }

    @Override
    public Basket getBasket(String basketOwnerId, User requester) {
        if (requesterIsOwnerOrAdmin(requester, basketOwnerId)) {
            return this.basketStore.getBasket(basketOwnerId);
        }else{
            throw new BasketActionException("Only basket owner or admin can get basket.");
        }
    }

    private boolean requesterIsOwnerOrAdmin(User requester, String basketOwnerId) {
        return requester.getRole() == ADMIN || requester.getUsername().equals(basketOwnerId);
    }
}
