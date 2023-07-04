package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.deal.DiscountDto;
import io.github.bullishtronics.checkout.io.order.ProductPurchaseDetailsDto;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.order.ProductPurchaseDetails;

import java.util.List;

public class ProductPurchaseDetailsDtoAdapter {
    private final DiscountDtoAdapter discountDtoAdapter;

    public ProductPurchaseDetailsDtoAdapter(DiscountDtoAdapter discountDtoAdapter) {
        this.discountDtoAdapter = discountDtoAdapter;
    }

    public ProductPurchaseDetailsDto adapte(ProductPurchaseDetails ppd) {
        return new ProductPurchaseDetailsDto(
                ppd.getProductId(),
                ppd.getProductName(),
                ppd.getRawPricePerUnit(),
                ppd.getQuantity(),
                build(ppd.getItemSpecificDiscounts())
        );
    }

    private List<DiscountDto> build(List<Discount> itemSpecificDiscounts) {
        return itemSpecificDiscounts == null ? List.of() : itemSpecificDiscounts.stream()
                .map(discountDtoAdapter::adapte)
                .toList();
    }
}
