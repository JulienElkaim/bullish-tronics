package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;
import io.github.bullishtronics.checkout.io.invoice.InvoiceDto;
import io.github.bullishtronics.checkout.io.order.ProductPurchaseDetailsDto;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.invoice.Invoice;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;

import java.util.List;

public class InvoiceDtoAdapter {
    private final ProductPurchaseDetailsDtoAdapter productPurchaseDetailsDtoAdapter;
    private final DiscountDtoAdapter discountDtoAdapter;

    public InvoiceDtoAdapter(ProductPurchaseDetailsDtoAdapter productPurchaseDetailsDtoAdapter, DiscountDtoAdapter discountDtoAdapter) {
        this.productPurchaseDetailsDtoAdapter = productPurchaseDetailsDtoAdapter;
        this.discountDtoAdapter = discountDtoAdapter;
    }

    public InvoiceDto adapte(Invoice invoice) {
        return new InvoiceDto(
                invoice.getOrderId(),
                invoice.getUserName(),
                invoice.getUserAddress(),
                buildProductPurchaseDetails(invoice.getProductPurchaseDetails()),
                buildGlobalDiscount(invoice.getGlobalDiscounts()),
                invoice.getTotalPrice()
        );
    }

    private List<DiscountDto> buildGlobalDiscount(List<Discount> globalDiscounts) {
        return globalDiscounts == null ? List.of() : globalDiscounts.stream()
                .map(discountDtoAdapter::adapte)
                .toList();
    }

    private List<ProductPurchaseDetailsDto> buildProductPurchaseDetails(List<ProductPurchaseDetails> productPurchaseDetails) {
        return productPurchaseDetails == null ? List.of() : productPurchaseDetails.stream()
                .map(productPurchaseDetailsDtoAdapter::adapte)
                .toList();
    }
}
