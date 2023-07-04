package io.github.bullishtronics.checkout.app.config;

import io.github.bullishtronics.checkout.core.deal.impl.DealFactory;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.purchase.impl.OrderFactory;
import io.github.bullishtronics.checkout.core.purchase.impl.QuotationFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeansConfig {
    @Bean
    public OrderFactory orderFactory(ProductService productService) {
        return new OrderFactory(productService);
    }

    @Bean
    public QuotationFactory quotationFactory() {
        return new QuotationFactory();
    }

    @Bean
    public DealFactory dealFactory() {
        return new DealFactory();
    }
}
