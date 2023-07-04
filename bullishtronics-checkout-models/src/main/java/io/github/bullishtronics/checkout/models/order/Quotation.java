package io.github.bullishtronics.checkout.models.order;

import java.time.Instant;

public class Quotation {
    private final String quotationId;
    private final Instant endValidityTime;
    private final String basketOwnerId;
    private final QuotationDetails quotationDetails;

    public Quotation(String quotationId, Instant endValidityTime, String basketOwnerId, QuotationDetails quotationDetails) {
        this.quotationId = quotationId;
        this.endValidityTime = endValidityTime;
        this.basketOwnerId = basketOwnerId;
        this.quotationDetails = quotationDetails;
    }

    public String getQuotationId() {
        return quotationId;
    }

    public Instant getEndValidityTime() {
        return endValidityTime;
    }

    public String getBasketOwnerId() {
        return basketOwnerId;
    }

    public QuotationDetails getQuotationDetails() {
        return quotationDetails;
    }
}
