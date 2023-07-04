package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemCombinationTargetPercentDiscounter extends Discounter<ItemCombinationTargetPercentDeal> {
    private final ProductService productService;

    public ItemCombinationTargetPercentDiscounter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public DealType getApplicableDealType() {
        return DealType.ITEM_COMBINATION_TARGET_PERCENT;
    }

    @Override
    protected Discount compute(Basket basket, ItemCombinationTargetPercentDeal deal) {
        Integer percent = deal.getPercent() % 100;
        String targetProductId = deal.getTargetProductId();
        Set<String> productIdsToCombine = deal.getProductIdsToCombine();
        if (percent < 1
                || targetProductId == null
                || productIdsToCombine == null
                || productIdsToCombine.size() < 2
                || !productIdsToCombine.contains(targetProductId)) {
            return null;
        }

        Map<String, Integer> productsByQuantity = basket.getProductsByQuantity();
        if (!productsByQuantity.keySet().containsAll(productIdsToCombine)) {
            return null;
        }

        Map<String, Product> productMap = getProductMap(productIdsToCombine);

        Integer minMultiplierForDiscount = productsByQuantity.entrySet().stream()
                .filter(entry -> productIdsToCombine.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .min(Integer::compare)
                .orElse(0);

        Product product = productMap.get(targetProductId);
        BigDecimal amountToDiscount = product.getPrice().setScale(2, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(minMultiplierForDiscount));

        return new Discount(targetProductId, amountToDiscount, deal.getTagline(), true);
    }

    @Override
    protected Class<ItemCombinationTargetPercentDeal> getSupportedDealClass() {
        return ItemCombinationTargetPercentDeal.class;
    }

    private Map<String, Product> getProductMap(Set<String> productIdsToCombine) {
        Map<String, Product> result = new HashMap<>();
        for (String productId : productIdsToCombine) {
            Product product = productService.getById(productId);
            if (product == null) {
                throw new DiscountActionException("Can't compute discount, one product does not exist: " + productId);
            }
            result.put(productId, product);
        }
        return result;
    }
}
