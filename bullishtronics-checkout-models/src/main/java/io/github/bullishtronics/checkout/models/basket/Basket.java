package io.github.bullishtronics.checkout.models.basket;

import java.util.Map;

public class Basket {
    private final String ownerId;
    private final Map<String,Integer> productsByQuantity;

    public static Basket empty(String ownerId){
        return new Basket(ownerId, Map.of());
    }

    public Basket(String ownerId, Map<String, Integer> productsByQuantity) {
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
