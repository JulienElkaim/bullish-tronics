package io.github.bullishtronics.checkout.io.order;

import java.time.Instant;

public class QuotationDto {
    private final String quotationId;
    private final Instant endValidityTime;
    private final String basketOwnerId;
    private final QuotationDetailsDto quotationDetails;

    public QuotationDto(String quotationId, Instant endValidityTime, String basketOwnerId, QuotationDetailsDto quotationDetails) {
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

    public QuotationDetailsDto getQuotationDetails() {
        return quotationDetails;
    }
}
