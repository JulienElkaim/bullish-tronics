package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.purchase.OrderStore;
import io.github.bullishtronics.checkout.core.purchase.exception.OrderActionException;
import io.github.bullishtronics.checkout.models.order.Order;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderStoreImpl implements OrderStore {
    private final ConcurrentHashMap<String, Order> orderMap;
    private final ConcurrentHashMap<String, Set<String>> userIdToOrderIds = new ConcurrentHashMap<>(); // faking DB index

    public OrderStoreImpl(List<Order> ordersInDbAtStart) {
        this.orderMap = new ConcurrentHashMap<>();
        for (Order order : ordersInDbAtStart) {
            save(order);
        }
    }

    @Override
    public void save(Order order) {
        String orderId = order.getOrderId();
        String userId = order.getUserId();
        if (orderId == null) {
            throw new OrderActionException("Can't persist order with order ID null");
        }
        if (userId == null) {
            throw new OrderActionException("Can't persist order with User ID null");
        }
        orderMap.compute(orderId, (id, record) -> {
            userIdToOrderIds.compute(userId, (uId, orderIds) -> {
                Set<String> result = orderIds == null
                        ? new HashSet<>()
                        : orderIds;
                result.add(orderId);
                return result;
            });
            return order;
        });
    }

    @Override
    public List<Order> getByUserId(String userId) {
        Set<String> orderIds = Objects.requireNonNullElse(userIdToOrderIds.get(userId), Set.of());
        return orderIds.stream()
                .map(this::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Order getById(String orderId) {
        return orderMap.get(orderId);
    }
}
