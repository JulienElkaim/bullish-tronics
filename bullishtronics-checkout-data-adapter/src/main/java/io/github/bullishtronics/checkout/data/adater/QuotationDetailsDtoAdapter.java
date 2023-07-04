package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;
import io.github.bullishtronics.checkout.io.order.QuotationDetailsDto;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.QuotationDetails;

import java.util.List;

public class QuotationDetailsDtoAdapter {
    private final DiscountDtoAdapter discountDtoAdapter;

    public QuotationDetailsDtoAdapter(DiscountDtoAdapter discountDtoAdapter) {
        this.discountDtoAdapter = discountDtoAdapter;
    }

    public QuotationDetailsDto adapte(QuotationDetails quotationDetails) {
        return new QuotationDetailsDto(
                quotationDetails.getProductsByQuantity(),
                build(quotationDetails.getDiscounts())
        );
    }

    private List<DiscountDto> build(List<Discount> discounts) {
        return discounts == null ? List.of() : discounts.stream()
                .map(discountDtoAdapter::adapte)
                .toList();
    }
}
