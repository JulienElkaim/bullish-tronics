package io.github.bullishtronics.checkout.core.purchase.impl;

import io.github.bullishtronics.checkout.models.order.Quotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class QuotationStoreImplTest {
    private QuotationStoreImpl quotationStore;
    private final String basketOwnerId = "basketOwnerId";
    private final String basketOwnerId2 = "basketOwnerId2";
    private final Quotation quotation = mock(Quotation.class);
    private final Quotation quotation2 = mock(Quotation.class);

    @BeforeEach
    void init(){
        reInit(Map.of(basketOwnerId, quotation));
    }

    @Test
    void save_recordNotExist_thenStored() {
        assertNull(quotationStore.getQuotationByOwnerId(basketOwnerId2));
        quotationStore.save(basketOwnerId2, quotation2);
        assertSame(quotation2, quotationStore.getQuotationByOwnerId(basketOwnerId2));
    }

    @Test
    void save_recordExist_thenUpdated() {
        assertSame(quotation, quotationStore.getQuotationByOwnerId(basketOwnerId));
        quotationStore.save(basketOwnerId, quotation2);
        assertSame(quotation2, quotationStore.getQuotationByOwnerId(basketOwnerId));
    }

    @Test
    void getQuotationByOwnerId_whenRecordExist_thenGetRecord() {
        assertSame(quotation, quotationStore.getQuotationByOwnerId(basketOwnerId));
    }

    @Test
    void getQuotationByOwnerId_whenRecordNotExist_thenNull() {
        assertNull(quotationStore.getQuotationByOwnerId(basketOwnerId2));
    }

    private void reInit(Map<String,Quotation> cache){
        quotationStore = new QuotationStoreImpl(cache);
    }
}