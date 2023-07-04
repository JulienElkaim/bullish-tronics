package io.github.bullishtronics.checkout.app.config;

import io.github.bullishtronics.checkout.core.basket.BasketStore;
import io.github.bullishtronics.checkout.core.basket.impl.BasketStoreImpl;
import io.github.bullishtronics.checkout.core.deal.DealStore;
import io.github.bullishtronics.checkout.core.deal.impl.DealStoreImpl;
import io.github.bullishtronics.checkout.core.product.ProductStore;
import io.github.bullishtronics.checkout.core.product.impl.ProductStoreImpl;
import io.github.bullishtronics.checkout.core.purchase.OrderStore;
import io.github.bullishtronics.checkout.core.purchase.QuotationStore;
import io.github.bullishtronics.checkout.core.purchase.impl.OrderStoreImpl;
import io.github.bullishtronics.checkout.core.purchase.impl.QuotationStoreImpl;
import io.github.bullishtronics.checkout.core.user.StringEncrypter;
import io.github.bullishtronics.checkout.core.user.UserStore;
import io.github.bullishtronics.checkout.core.user.impl.UserStoreImpl;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.product.Product;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class StoreBeansConfig {
    public static final String ADMIN_USERNAME = "admin";
    public static final String USER_USERNAME = "user";
    public static final String BUYER_USERNAME = "buyer";

    public static final String PRODUCT_1_ID = "product1";
    public static final String PRODUCT_2_ID = "product2";
    public static final String PRODUCT_3_ID = "product3";
    public static final String PRODUCT_4_ID = "product4";

    public static final String DEAL_1_ID = "DEAL1";
    public static final String DEAL_2_ID = "DEAL2";
    public static final String DEAL_3_ID = "DEAL3";
    public static final String DEAL_4_ID = "DEAL4";

    @Bean
    public ProductStore productStore() {
        Map<String, Product> fakeDbContentAtStart = Map.of(
                PRODUCT_1_ID, new Product(PRODUCT_1_ID, "Samsung Micro SD 256GB", BigDecimal.valueOf(18.6), "A micro SD card 300gb/s speed.", ADMIN_USERNAME),
                PRODUCT_2_ID, new Product(PRODUCT_2_ID, "Middle Sized Dog Crate Digital Locker", BigDecimal.valueOf(80), "Your Buddy's bed is now sage! Only available in fuchsia.", ADMIN_USERNAME),
                PRODUCT_3_ID, new Product(PRODUCT_3_ID, "Luna White Paper E-Book Ltd Edition", BigDecimal.valueOf(8.60), "E-Book of Luna white paper. Btw it's free on internet...", ADMIN_USERNAME),
                PRODUCT_4_ID, new Product(PRODUCT_4_ID, "Bitcoin White Paper E-Book Ltd Edition", BigDecimal.valueOf(308.99), "E-Book of Bitcoin white paper. 9 pages of loves. Btw it's free on internet...", ADMIN_USERNAME)
        );
        return new ProductStoreImpl(fakeDbContentAtStart);
    }

    @Bean
    public BasketStore basketStore() {
        Map<String, Basket> fakeDbContentAtStart = Map.of(
                BUYER_USERNAME, new Basket(BUYER_USERNAME, Map.of(
                        PRODUCT_1_ID, 10,
                        PRODUCT_2_ID, 1
                ))
        );
        return new BasketStoreImpl(fakeDbContentAtStart);
    }

    @Bean
    public DealStore dealStore() {
        Instant inThreeDays = Instant.now().plusSeconds(Duration.of(3, ChronoUnit.DAYS).toSeconds());
        Map<String, Deal> fakeDbContentAtStart = Map.of(
                DEAL_1_ID,
                new GlobalDeal(DEAL_1_ID, "18% OFF ON EVERYTHING - Let's Celebrate the store's 18 months of activity!", Instant.now(), inThreeDays, 18),
                DEAL_2_ID,
                new ItemModuloLastPercentDeal(DEAL_2_ID, PRODUCT_1_ID, 3, 80, "Buy 2 items, get the 3rd one at 80% OFF", Instant.now(), inThreeDays),
                DEAL_3_ID,
                new ItemCombinationTargetPercentDeal(DEAL_3_ID, PRODUCT_4_ID, 30, "Crypto Days! 30% OFF on Bitcoin's white paper if you buy the Luna's one.", Instant.now(), inThreeDays, Set.of(PRODUCT_3_ID, PRODUCT_4_ID)),
                DEAL_4_ID,
                new UserLocationContainsPercentDeal(DEAL_4_ID, "All Hong Kong customers will have 10% OFF !", Instant.now(), inThreeDays, 10, "Hong Kong")
        );
        return new DealStoreImpl(fakeDbContentAtStart);
    }

    @Bean
    public OrderStore orderStore() {
        List<Order> fakeDbContentAtStart = List.of();
        return new OrderStoreImpl(fakeDbContentAtStart);
    }

    @Bean
    public QuotationStore quotationStore() {
        Map<String, Quotation> fakeDbContentAtStart = Map.of();
        return new QuotationStoreImpl(fakeDbContentAtStart);
    }

    @Bean
    public UserStore userStore(StringEncrypter stringEncrypter) {
        Map<String, User> fakeDbContentAtStart = Map.of(
                ADMIN_USERNAME, new User(ADMIN_USERNAME, stringEncrypter.encrypt(ADMIN_USERNAME), Role.ADMIN, "Hillier Commercial Building", "John Admin Doe"),
                USER_USERNAME, new User(USER_USERNAME, stringEncrypter.encrypt(USER_USERNAME), Role.CUSTOMER, "Somewhere in Hong Kong", "John User Doe"),
                BUYER_USERNAME, new User(BUYER_USERNAME, stringEncrypter.encrypt(BUYER_USERNAME), Role.CUSTOMER, "Somewhere in Hong Kong", "John Buyer Doe")
        );
        return new UserStoreImpl(fakeDbContentAtStart);
    }
}
