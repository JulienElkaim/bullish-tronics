package io.github.bullishtronics.checkout.core.deal;

import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;
import io.github.bullishtronics.checkout.models.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class DiscounterTemplateTest<T extends Deal> {
    protected final Basket basket = mock(Basket.class);
    protected final ProductService productService = mock(ProductService.class);
    protected final String productId = "productId";
    protected final Product product = mock(Product.class);
    protected final BigDecimal productPrice = BigDecimal.TEN;
    protected final Integer productInitialQuantity = 1;

    protected Discounter<T> discounter;

    protected abstract Class<T> assignableDealClass();
    protected abstract DealType getApplicableDealType();

    @BeforeEach
    void initShared(){
        when(productService.getById(productId)).thenReturn(product);
        when(product.getPrice()).thenReturn(productPrice);
        when(basket.getProductsByQuantity()).thenReturn(Map.of(productId,productInitialQuantity));
    }

    @Test
    void computeDeal_whenProvidedDealOfNotTheRightType_throw() {
        Class<? extends Deal> aClass = nonSupportedDealClass();
        Deal dealMocked = mock(aClass);
        Assertions.assertThrows(DiscountActionException.class, () -> discounter.computeDeal(basket, dealMocked));
    }

    @Test
    void getApplicableDealTypeVerify(){
        assertEquals(getApplicableDealType(), discounter.getApplicableDealType());
    }

    private Class<? extends Deal> nonSupportedDealClass() {
        return UserLocationContainsPercentDeal.class.equals(assignableDealClass())
                ? GlobalDeal.class
                : UserLocationContainsPercentDeal.class;
    }
}