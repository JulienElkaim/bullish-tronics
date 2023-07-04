package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.basket.BasketService;
import io.github.bullishtronics.checkout.core.deal.DiscounterService;
import io.github.bullishtronics.checkout.core.purchase.OrderStore;
import io.github.bullishtronics.checkout.core.purchase.PurchaseService;
import io.github.bullishtronics.checkout.core.purchase.QuotationStore;
import io.github.bullishtronics.checkout.core.purchase.exception.OrderActionException;
import io.github.bullishtronics.checkout.core.purchase.exception.QuotationActionException;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class PurchaseServiceImpl implements PurchaseService {
    private final DiscounterService discounterService;
    private final UserService userService;
    private final QuotationStore quotationStore;
    private final QuotationFactory quotationFactory;
    private final OrderStore orderStore;
    private final OrderFactory orderFactory;
    private final BasketService basketService;

    private static final Duration DEFAULT_QUOTATION_DURATION = Duration.of(2, ChronoUnit.MINUTES);

    public PurchaseServiceImpl(DiscounterService discounterService,
                               UserService userService,
                               QuotationStore quotationStore,
                               QuotationFactory quotationFactory,
                               OrderStore orderStore,
                               OrderFactory orderFactory,
                               BasketService basketService) {
        this.discounterService = discounterService;
        this.userService = userService;
        this.quotationStore = quotationStore;
        this.quotationFactory = quotationFactory;
        this.orderStore = orderStore;
        this.orderFactory = orderFactory;
        this.basketService = basketService;
    }

    @Override
    public Quotation quote(String basketOwnerId, User requester) {
        if (requester.getRole() != Role.ADMIN && !requester.getUsername().equals(basketOwnerId)) {
            throw new QuotationActionException("Can't perform quotation as the requester is not admin or the owner of the submitted basket. Owner is: " + basketOwnerId);
        }

        Basket basket = basketService.getBasket(basketOwnerId, requester);
        User basketOwner = this.userService.getByUsername(basket.getOwnerId());
        Quotation quotation;

        try {
            List<Discount> discountsToApply = this.discounterService.compute(basket);
            quotation = quotationFactory.create(basket,
                    discountsToApply,
                    DEFAULT_QUOTATION_DURATION,
                    basketOwner);
        } catch (Exception e) {
            throw new QuotationActionException("Error while creating quotation: %s".formatted(e.getMessage()));
        }

        quotationStore.save(basketOwnerId, quotation);
        return quotation;
    }

    @Override
    public Order placeOrder(String quotationId, User requester) {
        Quotation quotation = this.quotationStore.getQuotationByOwnerId(requester.getUsername());
        if (quotation == null) {
            throw new OrderActionException("Can't find any quotation for this user.");
        }

        if (!quotation.getQuotationId().equals(quotationId)) {
            throw new OrderActionException("Quotation you have provided does not exist or is not available anymore. You provided %s and now available for this user is: %s".formatted(
                    quotationId,
                    quotation.getQuotationId())
            );
        }
        if (hasExpired(quotation)) {
            throw new OrderActionException("Can't place the order as the quotation is expired: " + quotation.getEndValidityTime());
        }

        Order order = orderFactory.generateOrderRecord(quotation);
        this.orderStore.save(order);
        return order;
    }

    @Override
    public List<Order> getMyOrders(User requester) {
        return Objects.requireNonNullElse(
                this.orderStore.getByUserId(requester.getUsername()),
                List.of()
        );
    }

    @Override
    public Order getById(String orderId, User requester) {
        Order order = this.orderStore.getById(orderId);
        if (requester.getRole() == Role.ADMIN || requester.getUsername().equals(order.getUserId())) {
            return order;
        }
        throw new OrderActionException("Can't get the order as the requester is not admin or the owner of the order: " + orderId);
    }

    private boolean hasExpired(Quotation quotation) {
        Instant endValidityTime = quotation.getEndValidityTime();
        return endValidityTime != null && Instant.now().isAfter(endValidityTime);
    }
}
