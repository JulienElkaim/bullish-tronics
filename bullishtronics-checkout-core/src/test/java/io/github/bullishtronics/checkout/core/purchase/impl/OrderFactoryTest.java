package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.purchase.exception.OrderActionException;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.order.QuotationDetails;
import io.github.bullishtronics.checkout.models.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderFactoryTest {
    private final ProductService productService = mock(ProductService.class);
    private final Quotation quotation = mock(Quotation.class);
    private final QuotationDetails quotationDetails = mock(QuotationDetails.class);
    private final Discount globalDiscount = mock(Discount.class);
    private final Discount productDiscount = mock(Discount.class);
    private final String productId = "productId";
    private final Product product = mock(Product.class);
    private final String productName = "productName";
    private final BigDecimal productPrice = BigDecimal.TEN;
    private OrderFactory orderFactory;

    @BeforeEach
    void init(){
        orderFactory = new OrderFactory(productService);

        when(quotation.getQuotationDetails()).thenReturn(quotationDetails);
        when(productDiscount.getProductId()).thenReturn(productId);
        when(globalDiscount.getProductId()).thenReturn(null);
        when(quotationDetails.getDiscounts()).thenReturn(List.of(globalDiscount, productDiscount));
        when(quotationDetails.getProductsByQuantity()).thenReturn(Map.of(productId, 1));
        when(productService.getById(productId)).thenReturn(product);
        when(product.getName()).thenReturn(productName);
        when(product.getPrice()).thenReturn(productPrice);
    }

    @Test
    void generateOrderRecord_thenSuccess(){
        Order result = orderFactory.generateOrderRecord(quotation);

        verify(productService).getById(productId);
        assertEquals(1, result.getProductPurchaseDetails().size());
        ProductPurchaseDetails productPurchaseDetails = result.getProductPurchaseDetails().get(0);
        assertEquals(productId, productPurchaseDetails.getProductId());
        assertEquals(productName, productPurchaseDetails.getProductName());
        assertEquals(productPrice, productPurchaseDetails.getRawPricePerUnit());
        assertEquals(1, productPurchaseDetails.getQuantity());
        assertEquals(List.of(productDiscount), productPurchaseDetails.getItemSpecificDiscounts());
    }

    @Test
    void generateOrderRecord_butProductDoesNotExist_thenThrowException(){
        when(productService.getById(productId)).thenReturn(null);
        OrderActionException exception = assertThrows(OrderActionException.class, () -> orderFactory.generateOrderRecord(quotation));
        assertEquals("Product not available anymore: " + productId, exception.getMessage());
    }

}