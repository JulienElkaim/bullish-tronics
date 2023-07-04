package io.github.bullishtronics.checkout.core.deal.impl.discounters;

import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import io.github.bullishtronics.checkout.models.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class ItemModuloLastPercentDiscounter extends Discounter<ItemModuloLastPercentDeal> {
    private final ProductService productService;

    public ItemModuloLastPercentDiscounter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public DealType getApplicableDealType() {
        return DealType.ITEM_MODULO_LAST_ITEM_PERCENT;
    }

    @Override
    protected Discount compute(Basket basket, ItemModuloLastPercentDeal deal) {
        String concernedProductId = deal.getProductId();
        Integer moduloNumber = deal.getModuloNumber();
        Integer percent = deal.getPercent() % 100;
        Map<String, Integer> productsByQuantity = basket.getProductsByQuantity();
        if (percent < 1 || moduloNumber < 1 || concernedProductId == null || !productsByQuantity.containsKey(concernedProductId)) {
            return null;
        }

        Integer quantity = productsByQuantity.get(concernedProductId);
        if (quantity % moduloNumber == quantity) {
            return null;
        }

        int nbOfItemsToDiscount = quantity / moduloNumber;
        Product product = productService.getById(concernedProductId);
        if (product == null) {
            throw new DiscountActionException("Can't compute discount, product does not exist: " + concernedProductId);
        }
        BigDecimal amountToDiscount = product.getPrice().setScale(2, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(nbOfItemsToDiscount));
        return new Discount(concernedProductId, amountToDiscount, deal.getTagline(), true);
    }

    @Override
    protected Class<ItemModuloLastPercentDeal> getSupportedDealClass() {
        return ItemModuloLastPercentDeal.class;
    }
}
