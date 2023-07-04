package io.github.bullishtronics.checkout.core.purchase;

import io.github.bullishtronics.checkout.models.order.Quotation;

public interface QuotationStore {
    void save(String basketOwnerId, Quotation quotation);

    Quotation getQuotationByOwnerId(String userId);
}
