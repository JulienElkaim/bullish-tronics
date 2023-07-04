package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.product.ProductDto;
import io.github.bullishtronics.checkout.models.product.Product;

public class ProductDtoAdapter {
    public ProductDto adapte(Product product){
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getVendorId()
        );
    }
}
