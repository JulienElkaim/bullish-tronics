package io.github.bullishtronics.checkout.core.purchase;

import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.user.User;

import java.util.List;

/**
 * Take a Basket, apply all the deals, and return an Invoice. Clear basket.
 */
public interface PurchaseService {
    Quotation quote(String basketOwnerId, User requester);

    Order placeOrder(String quotationId, User requester);

    List<Order> getMyOrders(User requester);
    Order getById(String orderId, User requester);
}
