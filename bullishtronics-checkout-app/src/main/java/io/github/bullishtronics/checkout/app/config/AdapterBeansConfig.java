package io.github.bullishtronics.checkout.app.config;

import io.github.bullishtronics.checkout.data.adater.BasketDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.DealDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.DiscountDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.GlobalDealDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.InvoiceDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.ItemCombinationTargetPercentDealDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.ItemModuloLastPercentDealDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.OrderDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.ProductDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.ProductPurchaseDetailsDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.QuotationDetailsDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.QuotationDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.UserDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.UserLocationContainsPercentDealDtoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterBeansConfig {
    @Bean
    public BasketDtoAdapter basketDtoAdapter() {
        return new BasketDtoAdapter();
    }

    @Bean
    public DealDtoAdapter dealDtoAdapter(GlobalDealDtoAdapter globalDealDtoAdapter,
                                         ItemModuloLastPercentDealDtoAdapter imlpDealDtoAdapter,
                                         ItemCombinationTargetPercentDealDtoAdapter ictpDealDtoAdapter,
                                         UserLocationContainsPercentDealDtoAdapter ulcpDealDtoAdapter) {
        return new DealDtoAdapter(imlpDealDtoAdapter, globalDealDtoAdapter, ictpDealDtoAdapter, ulcpDealDtoAdapter);
    }

    @Bean
    public GlobalDealDtoAdapter globalDealDtoAdapter() {
        return new GlobalDealDtoAdapter();
    }

    @Bean
    public ItemModuloLastPercentDealDtoAdapter imlpDealDtoAdapter() {
        return new ItemModuloLastPercentDealDtoAdapter();
    }

    @Bean
    public ItemCombinationTargetPercentDealDtoAdapter ictpDealDtoAdapter() {
        return new ItemCombinationTargetPercentDealDtoAdapter();
    }

    @Bean
    public UserLocationContainsPercentDealDtoAdapter ulcpDealDtoAdapter() {
        return new UserLocationContainsPercentDealDtoAdapter();
    }

    @Bean
    public DiscountDtoAdapter discountDtoAdapter() {
        return new DiscountDtoAdapter();
    }

    @Bean
    public InvoiceDtoAdapter invoiceDtoAdapter(ProductPurchaseDetailsDtoAdapter ppdDtoAdapter,
                                               DiscountDtoAdapter discountDtoAdapter) {
        return new InvoiceDtoAdapter(ppdDtoAdapter, discountDtoAdapter);
    }

    @Bean
    public OrderDtoAdapter orderDtoAdapter(ProductPurchaseDetailsDtoAdapter ppdDtoAdapter,
                                           DiscountDtoAdapter discountDtoAdapter) {
        return new OrderDtoAdapter(ppdDtoAdapter, discountDtoAdapter);
    }

    @Bean
    public ProductDtoAdapter productDtoAdapter() {
        return new ProductDtoAdapter();
    }

    @Bean
    public ProductPurchaseDetailsDtoAdapter productPurchaseDetailsDtoAdapter(DiscountDtoAdapter discountDtoAdapter) {
        return new ProductPurchaseDetailsDtoAdapter(discountDtoAdapter);
    }

    @Bean
    public QuotationDetailsDtoAdapter quotationDetailsDtoAdapter(DiscountDtoAdapter discountDtoAdapter) {
        return new QuotationDetailsDtoAdapter(discountDtoAdapter);
    }

    @Bean
    public QuotationDtoAdapter quotationDtoAdapter(QuotationDetailsDtoAdapter quotationDetailsDtoAdapter) {
        return new QuotationDtoAdapter(quotationDetailsDtoAdapter);
    }

    @Bean
    public UserDtoAdapter userDtoAdapter() {
        return new UserDtoAdapter();
    }
}
