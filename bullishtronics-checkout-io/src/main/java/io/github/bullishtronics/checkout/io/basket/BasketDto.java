package io.github.bullishtronics.checkout.io.basket;

import java.util.Map;

public class BasketDto {
    private final String ownerId;
    private final Map<String,Integer> productsByQuantity;

    public BasketDto(String ownerId, Map<String, Integer> productsByQuantity) {
        this.ownerId = ownerId;
        this.productsByQuantity = Map.copyOf(productsByQuantity);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Map<String, Integer> getProductsByQuantity() {
        return productsByQuantity;
    }
}
