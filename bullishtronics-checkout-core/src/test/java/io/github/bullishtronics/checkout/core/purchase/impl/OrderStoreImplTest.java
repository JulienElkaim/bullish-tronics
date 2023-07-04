package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.purchase.exception.OrderActionException;
import io.github.bullishtronics.checkout.models.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderStoreImplTest {
    private OrderStoreImpl orderStore;
    private final String orderId = "orderId";
    private final String orderId2 = "orderId2";
    private final String userId = "userId";
    private final String userId2 = "userId2";
    private final Order order = mock(Order.class);
    private final Order order2 = mock(Order.class);

    @BeforeEach
    void init(){
        when(order.getOrderId()).thenReturn(orderId);
        when(order2.getOrderId()).thenReturn(orderId2);
        when(order.getUserId()).thenReturn(userId);
        when(order2.getUserId()).thenReturn(userId2);

        reInit(List.of(order));
    }

    @Test
    void save_newRecord_andUserAlreadyHadOne() {
        when(order2.getUserId()).thenReturn(userId);
        assertNull(orderStore.getById(orderId2));
        orderStore.save(order2);

        assertSame(order2, orderStore.getById(orderId2));
        List<Order> orders = orderStore.getByUserId(userId);
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order));
        assertTrue(orders.contains(order2));
    }

    @Test
    void save_newRecord_withNullOrderId() {
        when(order2.getOrderId()).thenReturn(null);
        assertNull(orderStore.getById(orderId2));

        OrderActionException exception = assertThrows(OrderActionException.class, () -> orderStore.save(order2));
        assertEquals("Can't persist order with order ID null", exception.getMessage());

        assertNull(orderStore.getById(orderId2));
    }

    @Test
    void save_newRecord_withNullUserId() {
        when(order2.getUserId()).thenReturn(null);
        assertNull(orderStore.getById(orderId2));

        OrderActionException exception = assertThrows(OrderActionException.class, () -> orderStore.save(order2));
        assertEquals("Can't persist order with User ID null", exception.getMessage());

        assertNull(orderStore.getById(orderId2));
    }

    @Test
    void getByUserId_existingRecord() {
        List<Order> orders = orderStore.getByUserId(userId);
        assertEquals(1, orders.size());
        assertSame(order, orders.get(0));
    }


    @Test
    void getByUserId_NonExistingRecord() {
        List<Order> orders = orderStore.getByUserId(userId2);
        assertTrue(orders.isEmpty());
    }

    @Test
    void getById_existingRecord() {
        Order result = orderStore.getById(orderId);
        assertSame(order, result);
    }

    @Test
    void getById_nonExistingRecord() {
        Order result = orderStore.getById(orderId2);
        assertNull(result);
    }

    private void reInit(List<Order> order) {
        orderStore = new OrderStoreImpl(order);
    }
}