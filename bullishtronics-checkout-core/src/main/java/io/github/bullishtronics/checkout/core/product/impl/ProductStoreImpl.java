package io.github.bullishtronics.checkout.core.product.impl;

import io.github.bullishtronics.checkout.core.product.ProductStore;
import io.github.bullishtronics.checkout.core.product.exception.ProductActionException;
import io.github.bullishtronics.checkout.models.product.Product;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductStoreImpl implements ProductStore {
    private final ConcurrentHashMap<String, Product> productMap;

    public ProductStoreImpl(Map<String, Product> fakeDbContentAtStart) {
        this.productMap = new ConcurrentHashMap<>(fakeDbContentAtStart);
    }

    @Override
    public void save(Product product) {
        if (product.getProductId() == null) {
            throw new ProductActionException("Can't persist a product with ID null");
        }
        this.productMap.put(product.getProductId(), product);
    }

    @Override
    public Product remove(String productId) {
        return this.productMap.remove(productId);
    }

    @Override
    public Product getById(String productId) {
        return productMap.get(productId);
    }

    @Override
    public List<Product> getAll() {
        return productMap.values().stream().toList();
    }
}
