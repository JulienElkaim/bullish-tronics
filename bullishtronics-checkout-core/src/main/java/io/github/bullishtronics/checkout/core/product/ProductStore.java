package io.github.bullishtronics.checkout.core.product;

import io.github.bullishtronics.checkout.models.product.Product;

import java.util.List;

public interface ProductStore {
    void save(Product product);
    Product remove(String productId);
    Product getById(String productId);
    List<Product> getAll();
}
