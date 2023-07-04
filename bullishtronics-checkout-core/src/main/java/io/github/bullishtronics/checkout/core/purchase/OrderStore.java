package io.github.bullishtronics.checkout.core.purchase;

import io.github.bullishtronics.checkout.models.order.Order;

import java.util.List;

public interface OrderStore {
    void save(Order order);

    List<Order> getByUserId(String userId);

    Order getById(String orderId);
}
