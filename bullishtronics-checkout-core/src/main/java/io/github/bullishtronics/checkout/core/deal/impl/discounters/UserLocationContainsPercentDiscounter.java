package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;
import io.github.bullishtronics.checkout.models.product.Product;
import io.github.bullishtronics.checkout.models.user.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class UserLocationContainsPercentDiscounter extends Discounter<UserLocationContainsPercentDeal> {
    private final ProductService productService;
    private final UserService userService;

    public UserLocationContainsPercentDiscounter(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public DealType getApplicableDealType() {
        return DealType.USER_LOCATION_CONTAINS_PERCENT;
    }

    @Override
    protected Discount compute(Basket basket, UserLocationContainsPercentDeal deal) {
        String locationSubtext = deal.getLocationSubtext();
        Integer percent = deal.getPercent() % 100;
        if(percent < 1 || locationSubtext == null || locationSubtext.isBlank()) {
            return null;
        }
        String ownerId = basket.getOwnerId();
        User basketOwner = userService.getByUsername(ownerId);

        if(basketOwner == null || basketOwner.getAddress() == null){
            return null;
        }

        if(!basketOwner.getAddress().toLowerCase().contains(locationSubtext.toLowerCase())){
            return null;
        }

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

        BigDecimal discountAmount = totalPriceBeforeDiscount
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN)
                .setScale(2, RoundingMode.DOWN);

        return new Discount(null, discountAmount, deal.getTagline(), false);
    }

    @Override
    protected Class<UserLocationContainsPercentDeal> getSupportedDealClass() {
        return UserLocationContainsPercentDeal.class;
    }
}
