package io.github.bullishtronics.checkout.app.config;


import io.github.bullishtronics.checkout.core.basket.BasketService;
import io.github.bullishtronics.checkout.core.basket.BasketStore;
import io.github.bullishtronics.checkout.core.basket.impl.BasketServiceImpl;
import io.github.bullishtronics.checkout.core.deal.DealService;
import io.github.bullishtronics.checkout.core.deal.DealStore;
import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.core.deal.DiscounterService;
import io.github.bullishtronics.checkout.core.deal.impl.DealFactory;
import io.github.bullishtronics.checkout.core.deal.impl.DealServiceImpl;
import io.github.bullishtronics.checkout.core.deal.impl.DiscounterServiceImpl;
import io.github.bullishtronics.checkout.core.deal.impl.discounters.GlobalPercentDiscounter;
import io.github.bullishtronics.checkout.core.deal.impl.discounters.ItemCombinationTargetPercentDiscounter;
import io.github.bullishtronics.checkout.core.deal.impl.discounters.ItemModuloLastPercentDiscounter;
import io.github.bullishtronics.checkout.core.deal.impl.discounters.UserLocationContainsPercentDiscounter;
import io.github.bullishtronics.checkout.core.invoice.InvoiceService;
import io.github.bullishtronics.checkout.core.invoice.impl.InvoiceServiceImpl;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.product.ProductStore;
import io.github.bullishtronics.checkout.core.product.impl.ProductServiceImpl;
import io.github.bullishtronics.checkout.core.purchase.OrderStore;
import io.github.bullishtronics.checkout.core.purchase.PurchaseService;
import io.github.bullishtronics.checkout.core.purchase.QuotationStore;
import io.github.bullishtronics.checkout.core.purchase.impl.OrderFactory;
import io.github.bullishtronics.checkout.core.purchase.impl.PurchaseServiceImpl;
import io.github.bullishtronics.checkout.core.purchase.impl.QuotationFactory;
import io.github.bullishtronics.checkout.core.user.StringEncrypter;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.core.user.UserStore;
import io.github.bullishtronics.checkout.core.user.impl.UserServiceImpl;
import io.github.bullishtronics.checkout.models.deal.DealType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ServiceBeansConfig {
    @Bean
    public ProductService productService(ProductStore productStore) {
        return new ProductServiceImpl(productStore);
    }

    @Bean
    public BasketService basketService(BasketStore basketStore) {
        return new BasketServiceImpl(basketStore);
    }

    @Bean
    public PurchaseService purchaseService(DiscounterService discounterService,
                                           BasketService basketService,
                                           UserService userService,
                                           QuotationStore quotationStore,
                                           QuotationFactory quotationFactory,
                                           OrderStore orderStore,
                                           OrderFactory orderFactory) {
        return new PurchaseServiceImpl(discounterService,
                userService,
                quotationStore,
                quotationFactory,
                orderStore,
                orderFactory,
                basketService);
    }

    @Bean
    public DiscounterService discounterService(DealService dealService,
                                               List<Discounter<?>> discounters) {
        Map<DealType, Discounter<?>> discounterMap = discounters.stream()
                .collect(Collectors.toMap(
                        Discounter::getApplicableDealType,     // key = discounter ID
                        discounter -> discounter,  // value = discounter
                        (discounter1, discounter2) -> {
                            throw new IllegalStateException("Multiple discounters for deal type: " + discounter1.getApplicableDealType());
                        }
                ));
        return new DiscounterServiceImpl(dealService, discounterMap);
    }

    @Bean
    public List<Discounter> allDiscounters(ProductService productService,
                                           UserService userService) {
        return List.of(
                new GlobalPercentDiscounter(productService),
                new ItemModuloLastPercentDiscounter(productService),
                new ItemCombinationTargetPercentDiscounter(productService),
                new UserLocationContainsPercentDiscounter(productService, userService)
        );
    }

    @Bean
    public DealService dealService(DealStore dealStore,
                                   DealFactory dealFactory) {
        return new DealServiceImpl(dealStore, dealFactory);
    }

    @Bean
    public InvoiceService invoiceService(PurchaseService purchaseService) {
        return new InvoiceServiceImpl(purchaseService);
    }

    @Bean
    public UserService userService(UserStore userStore, StringEncrypter stringEncrypter) {
        return new UserServiceImpl(userStore, stringEncrypter);
    }
}
