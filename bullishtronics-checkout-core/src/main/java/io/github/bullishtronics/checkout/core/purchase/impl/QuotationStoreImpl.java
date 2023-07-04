package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.core.purchase.QuotationStore;
import io.github.bullishtronics.checkout.models.order.Quotation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuotationStoreImpl implements QuotationStore {
    private final ConcurrentHashMap<String, Quotation> quotationMap;

    public QuotationStoreImpl(Map<String, Quotation> quotationInDBAtStart) {
        this.quotationMap = new ConcurrentHashMap<>(quotationInDBAtStart);
    }

    @Override
    public void save(String basketOwnerId, Quotation quotation) {
        quotationMap.put(basketOwnerId, quotation);
    }

    @Override
    public Quotation getQuotationByOwnerId(String userId) {
        return quotationMap.get(userId);
    }
}
