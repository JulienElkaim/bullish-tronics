package io.github.bullishtronics.checkout.core.product;

import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.product.CreateProductDetails;
import io.github.bullishtronics.checkout.models.product.Product;

import java.util.List;

/**
 * Manage the products available in the store
 */
public interface ProductService {
    Product create(CreateProductDetails productDetails, User requester);
    Product delete(String productId, User requester);
    Product getById(String productId);
    List<Product> getAll();
}
