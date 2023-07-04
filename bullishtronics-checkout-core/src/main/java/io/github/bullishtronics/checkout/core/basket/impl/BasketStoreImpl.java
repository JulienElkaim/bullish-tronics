package io.github.bullishtronics.checkout.core.basket.impl;

import io.github.bullishtronics.checkout.core.basket.BasketStore;
import io.github.bullishtronics.checkout.models.basket.Basket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BasketStoreImpl implements BasketStore {
    private final ConcurrentHashMap<String, Basket> basketMap;

    public BasketStoreImpl(Map<String, Basket> fakeDbContentAtStart) {
        this.basketMap = new ConcurrentHashMap<>(fakeDbContentAtStart);
    }

    @Override
    public Basket getBasket(String basketOwnerId) {
        return basketMap.getOrDefault(basketOwnerId, new Basket(basketOwnerId, Map.of()));
    }

    @Override
    public Basket add(String productId, Integer quantitiesToAdd, String basketOwnerId) {
        return basketMap.compute(basketOwnerId, (id, existingBasket) -> {
            if (existingBasket == null) {
                return new Basket(basketOwnerId, Map.of(productId, quantitiesToAdd));
            }
            Map<String, Integer> modifiableProductsMap = new HashMap<>(existingBasket.getProductsByQuantity());
            modifiableProductsMap.computeIfPresent(productId, (p, q) -> q + quantitiesToAdd);
            modifiableProductsMap.putIfAbsent(productId, quantitiesToAdd);

            return new Basket(basketOwnerId, modifiableProductsMap);
        });
    }

    @Override
    public Basket remove(String productId, Integer quantitiesToRemove, String basketOwnerId) {
        return basketMap.compute(basketOwnerId, (id, existingBasket) -> {
            if (existingBasket == null) {
                return null;
            }

            Map<String, Integer> modifiableProductsMap = new HashMap<>(existingBasket.getProductsByQuantity());
            modifiableProductsMap.computeIfPresent(productId, (p, q) -> {
                if (q - quantitiesToRemove <= 0) {
                    return null;
                }
                return q - quantitiesToRemove;
            });

            return modifiableProductsMap.isEmpty()
                    ? null
                    : new Basket(basketOwnerId, modifiableProductsMap);
        });
    }

    @Override
    public void clear(String basketOwnerId) {
        basketMap.remove(basketOwnerId);
    }
}
