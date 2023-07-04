package io.github.bullishtronics.checkout.models.product;

import java.math.BigDecimal;

public class Product {
    private final String productId;
    private final String name;
    private final BigDecimal price;
    private final String description;
    private final String vendorId;

    public Product(String productId, String name, BigDecimal price, String description, String vendorId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.vendorId = vendorId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
