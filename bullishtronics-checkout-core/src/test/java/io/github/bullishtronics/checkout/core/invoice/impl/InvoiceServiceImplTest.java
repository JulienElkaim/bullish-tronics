package io.github.bullishtronics.checkout.core.invoice.impl;

import io.github.bullishtronics.checkout.core.invoice.exception.InvoiceActionException;
import io.github.bullishtronics.checkout.core.purchase.PurchaseService;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.invoice.Invoice;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InvoiceServiceImplTest {
    private final PurchaseService purchaseService = mock(PurchaseService.class);

    private final Order order = mock(Order.class);
    private final String orderId = "orderId";
    private final Discount globalDiscount = mock(Discount.class);
    private final ProductPurchaseDetails productPurchaseDetails = mock(ProductPurchaseDetails.class);
    private final BigDecimal rawPricePerUnit = BigDecimal.TEN;
    private final int quantity = 1;
    private final String productId = "productId";
    private final Discount productDiscount = mock(Discount.class);

    private final User requester = mock(User.class);
    private final String userId = "userId";
    private final String userFullName = "userFullName";
    private final String address = "address";

    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    void init() {
        invoiceService = new InvoiceServiceImpl(purchaseService);

        when(purchaseService.getById(orderId, requester)).thenReturn(order);
        when(order.getUserId()).thenReturn(userId);
        when(requester.getUsername()).thenReturn(userId);
        when(requester.getUserFullName()).thenReturn(userFullName);
        when(requester.getAddress()).thenReturn(address);
        when(order.getProductPurchaseDetails()).thenReturn(List.of(productPurchaseDetails));
        when(order.getGlobalDiscounts()).thenReturn(List.of(globalDiscount));
        when(productPurchaseDetails.getRawPricePerUnit()).thenReturn(rawPricePerUnit);
        when(productPurchaseDetails.getQuantity()).thenReturn(quantity);
        when(productPurchaseDetails.getProductId()).thenReturn(productId);
        when(productPurchaseDetails.getItemSpecificDiscounts()).thenReturn(List.of(productDiscount));
        when(globalDiscount.getAmount()).thenReturn(BigDecimal.ONE);
        when(productDiscount.getAmount()).thenReturn(BigDecimal.ONE);
    }

    @Test
    void generate() {
        Invoice result = invoiceService.generate(orderId, requester);

        verify(purchaseService).getById(orderId, requester);
        assertEquals(orderId, result.getOrderId());
        assertEquals(userFullName, result.getUserName());
        assertEquals(address, result.getUserAddress());
        assertEquals(List.of(productPurchaseDetails), result.getProductPurchaseDetails());
        assertEquals(List.of(globalDiscount), result.getGlobalDiscounts());
        assertEquals(BigDecimal.valueOf(8), result.getTotalPrice());
    }

    @Test
    void generate_whenNoOrderMatchingId_thenThrowException() {
        when(purchaseService.getById(orderId, requester)).thenReturn(null);
        InvoiceActionException exception = assertThrows(InvoiceActionException.class, () -> invoiceService.generate(orderId, requester));

        assertEquals("Order not found: " + orderId, exception.getMessage());
    }

    @Test
    void generate_whenOrderNotOwnedByTheRequester_thenThrowException() {
        when(order.getUserId()).thenReturn("someoneElseForSure");
        InvoiceActionException exception = assertThrows(InvoiceActionException.class, () -> invoiceService.generate(orderId, requester));

        assertEquals("Only the Client can request for an invoice on its orders.", exception.getMessage());
    }
}