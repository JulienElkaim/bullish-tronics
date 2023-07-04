package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class GlobalPercentDiscounter extends Discounter<GlobalDeal> {
    private final ProductService productService;

    public GlobalPercentDiscounter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public DealType getApplicableDealType() {
        return DealType.GLOBAL_PERCENT;
    }

    @Override
    protected Discount compute(Basket basket, GlobalDeal deal) {
        Map<String, Product> productMap = new HashMap<>();
        Map<String, Integer> productsByQuantity = basket.getProductsByQuantity();
        for (String productId : productsByQuantity.keySet()) {
            Product product = productService.getById(productId);
            if(product == null) {
                throw new DiscountActionException("Can't compute discount, one product does not exist: " + productId);
            }
            productMap.put(productId, product);
        }

        BigDecimal totalPriceBeforeDiscount = productsByQuantity.entrySet().stream()
                .reduce(BigDecimal.ZERO, (acc, entry) -> {
                    Product product = productMap.get(entry.getKey());
                    BigDecimal productPrice = product.getPrice();
                    BigDecimal productQuantity = BigDecimal.valueOf(entry.getValue());

                    BigDecimal productTotalPrice = productPrice.multiply(productQuantity);
                    return acc.add(productTotalPrice);
                }, BigDecimal::add);

        Integer percent = deal.getPercent() % 100;
        BigDecimal discountAmount = totalPriceBeforeDiscount.setScale(2, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN);

        return new Discount(null, discountAmount, deal.getTagline(), false);
    }

    @Override
    protected Class<GlobalDeal> getSupportedDealClass() {
        return GlobalDeal.class;
    }
}
