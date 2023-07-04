package io.github.bullishtronics.checkout.core.invoice.impl;

import io.github.bullishtronics.checkout.core.invoice.InvoiceService;
import io.github.bullishtronics.checkout.core.invoice.exception.InvoiceActionException;
import io.github.bullishtronics.checkout.core.purchase.PurchaseService;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.invoice.Invoice;
import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.order.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceServiceImpl implements InvoiceService {
    private final PurchaseService purchaseService;

    public InvoiceServiceImpl(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Override
    public Invoice generate(String orderId, User requester) {
        Order order = purchaseService.getById(orderId, requester);
        if(order == null){
            throw new InvoiceActionException("Order not found: " + orderId);
        }

        if(!requester.getUsername().equals(order.getUserId())){
            throw new InvoiceActionException("Only the Client can request for an invoice on its orders.");
        }

        return new Invoice(
                orderId,
                requester.getUserFullName(),
                requester.getAddress(),
                order.getProductPurchaseDetails(),
                order.getGlobalDiscounts(),
                computeTotalPrice(order));
    }

    private BigDecimal computeTotalPrice(Order order) {
        BigDecimal priceWithoutGlobalDiscount = order.getProductPurchaseDetails().stream()
                .map(item-> {
                    BigDecimal rawPrice = item.getRawPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity()));
                    BigDecimal discounts = sumDiscountList(item.getItemSpecificDiscounts());

                    return rawPrice.subtract(discounts);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGlobalDiscounts = sumDiscountList(order.getGlobalDiscounts());

        return BigDecimal.ZERO.max(priceWithoutGlobalDiscount.subtract(totalGlobalDiscounts));
    }

    private BigDecimal sumDiscountList(List<Discount> discounts) {
        return discounts.stream()
                .map(Discount::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
