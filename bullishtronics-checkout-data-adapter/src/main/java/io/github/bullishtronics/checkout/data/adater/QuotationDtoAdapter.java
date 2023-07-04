package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.order.QuotationDto;
import io.github.bullishtronics.checkout.models.order.Quotation;

public class QuotationDtoAdapter {
    private final QuotationDetailsDtoAdapter quotationDetailsDtoAdapter;

    public QuotationDtoAdapter(QuotationDetailsDtoAdapter quotationDetailsDtoAdapter) {
        this.quotationDetailsDtoAdapter = quotationDetailsDtoAdapter;
    }

    public QuotationDto adapte(Quotation quotation) {
        return new QuotationDto(
                quotation.getQuotationId(),
                quotation.getEndValidityTime(),
                quotation.getBasketOwnerId(),
                quotationDetailsDtoAdapter.adapte(quotation.getQuotationDetails())
        );
    }
}
