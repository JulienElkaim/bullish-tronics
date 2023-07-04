package io.github.bullishtronics.checkout.core.product.impl;

import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.product.ProductStore;
import io.github.bullishtronics.checkout.core.product.exception.ProductActionException;
import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.product.CreateProductDetails;
import io.github.bullishtronics.checkout.models.product.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static io.github.bullishtronics.checkout.models.user.Role.ADMIN;

public class ProductServiceImpl implements ProductService {
    private final ProductStore productStore;

    public ProductServiceImpl(ProductStore productStore) {
        this.productStore = productStore;
    }

    @Override
    public Product create(CreateProductDetails productDetails, User user) {
        String failureReason = validateCreate(productDetails, user);

        if (failureReason != null) {
            throw new ProductActionException(failureReason);
        }

        Product product = new Product(UUID.randomUUID().toString(),
                productDetails.getName(),
                productDetails.getPrice(),
                productDetails.getDescription(),
                user.getUsername());

        try {
            productStore.save(product);
        } catch (Exception e) {
            throw new ProductActionException("Failed to save product for reason: " + e.getMessage());
        }

        return product;
    }

    @Override
    public Product delete(String productId, User user) {
        if (user.getRole() != ADMIN) {
            throw new ProductActionException("User should be admin to delete a product.");
        }
        Product product = this.productStore.remove(productId);
        if (product == null) {
            throw new ProductActionException(String.format("Product %s does not exist.", productId));
        } else {
            return product;
        }
    }

    @Override
    public Product getById(String productId) {
        return this.productStore.getById(productId);
    }

    @Override
    public List<Product> getAll() {
        return this.productStore.getAll();
    }

    private String validateCreate(CreateProductDetails productDetails, User user) {
        String failureReason = null;

        if (user.getRole() != ADMIN) {
            failureReason = "User should be admin to create a new product.";
        } else if (productDetails.getName().length() < 4) {
            failureReason = "Name should be insightful, please enter more than 4 characters.";
        } else if (productDetails.getPrice().compareTo(BigDecimal.ONE) < 0) {
            failureReason = "Price should be more than 1.";
        } else if (productDetails.getPrice().compareTo(new BigDecimal(99999)) > 0) {
            failureReason = "We do not sell Ferraris. Price should be less than 99999.";
        } else if (productDetails.getPrice().scale() > 2) {
            failureReason = "Price should have MAX 2 decimal digits.";
        }

        return failureReason;
    }
}
