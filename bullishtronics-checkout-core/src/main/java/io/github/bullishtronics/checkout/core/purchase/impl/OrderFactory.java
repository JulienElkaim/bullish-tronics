package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.purchase.exception.OrderActionException;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.order.QuotationDetails;
import io.github.bullishtronics.checkout.models.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderFactory {
    private final ProductService productService;

    public OrderFactory(ProductService productService) {
        this.productService = productService;
    }

    public Order generateOrderRecord(Quotation quotation) {
        QuotationDetails quotationDetails = quotation.getQuotationDetails();

        List<Discount> globalDiscounts = getGlobalDiscounts(quotationDetails.getDiscounts());
        Map<String, List<Discount>> productDiscountMap = getProductDiscountMap(quotationDetails.getDiscounts());
        Map<String, Integer> productsByQuantity = quotationDetails.getProductsByQuantity();

        return new Order(
                UUID.randomUUID().toString(),
                quotation.getBasketOwnerId(),
                buildProductDetails(productsByQuantity, productDiscountMap),
                globalDiscounts
        );
    }

    private List<ProductPurchaseDetails> buildProductDetails(Map<String, Integer> productsByQuantity, Map<String, List<Discount>> productDiscountMap) {
        List<ProductPurchaseDetails> result = new ArrayList<>();

        for (String productId : productsByQuantity.keySet()) {
            Product product = productService.getById(productId);
            if (product == null) {
                throw new OrderActionException("Product not available anymore: " + productId);
            }
            result.add(new ProductPurchaseDetails(
                    productId,
                    product.getName(),
                    product.getPrice(),
                    productsByQuantity.get(productId),
                    productDiscountMap.getOrDefault(productId, List.of())
            ));
        }

        return result;
    }

    private Map<String, List<Discount>> getProductDiscountMap(List<Discount> discounts) {
        return discounts.stream()
                .filter(discount -> discount.getProductId() != null)
                .collect(Collectors.groupingBy(
                        Discount::getProductId,
                        Collectors.mapping(discount -> discount, Collectors.toList())
                ));
    }

    private List<Discount> getGlobalDiscounts(List<Discount> discounts) {
        return discounts.stream()
                .filter(discount -> discount.getProductId() == null)
                .toList();
    }
}
