package io.github.bullishtronics.checkout.core.product.impl;

import io.github.bullishtronics.checkout.core.product.ProductStore;
import io.github.bullishtronics.checkout.core.product.exception.ProductActionException;
import io.github.bullishtronics.checkout.models.product.CreateProductDetails;
import io.github.bullishtronics.checkout.models.product.Product;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {
    private final String productId = "productId";
    private final Product product = mock(Product.class);
    private final CreateProductDetails productDetails = mock(CreateProductDetails.class);
    private final String productName = "productName";
    private final String productDescription = "productDescription";
    private final ProductStore productStore = mock(ProductStore.class);
    private final User user = mock(User.class);
    private final String username = "username";
    private ProductServiceImpl productService;

    @BeforeEach
    void init() {
        productService = new ProductServiceImpl(productStore);

        when(user.getRole()).thenReturn(Role.ADMIN);
        when(user.getUsername()).thenReturn(username);

        when(productStore.remove(productId)).thenReturn(product);
        when(productDetails.getName()).thenReturn(productName);
        when(productDetails.getDescription()).thenReturn(productDescription);
        when(productDetails.getPrice()).thenReturn(BigDecimal.TEN);
    }

    @Test
    void create_thenSuccess() {
        Product result = productService.create(productDetails, user);

        verify(productStore).save(result);
        assertNotNull(result.getProductId());
        assertEquals(productName, result.getName());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals(productName, result.getName());
        assertEquals(productDescription, result.getDescription());
        assertEquals(username, result.getVendorId());
    }

    @Test
    void create_butUserIsNotAdmin_thenThrowException() {
        when(user.getRole()).thenReturn(Role.CUSTOMER);

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.create(productDetails, user));

        verify(productStore, never()).save(any());
        assertEquals("User should be admin to create a new product.", exception.getMessage());
    }

    @Test
    void create_butProductNameIsLessThan4Chars_thenThrowException() {
        when(productDetails.getName()).thenReturn("abc");

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.create(productDetails, user));

        verify(productStore, never()).save(any());
        assertEquals("Name should be insightful, please enter more than 4 characters.", exception.getMessage());
    }

    @Test
    void create_butPriceIsLessThanOne_thenThrowException() {
        when(productDetails.getPrice()).thenReturn(BigDecimal.valueOf(0.97));

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.create(productDetails, user));

        verify(productStore, never()).save(any());
        assertEquals("Price should be more than 1.", exception.getMessage());
    }

    @Test
    void create_butPriceIsMoreThan999999_thenThrowException() {
        when(productDetails.getPrice()).thenReturn(BigDecimal.valueOf(99999.5));

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.create(productDetails, user));

        verify(productStore, never()).save(any());
        assertEquals("We do not sell Ferraris. Price should be less than 99999.", exception.getMessage());
    }

    @Test
    void create_butPriceHasMoreThan2DigitsDecimals_thenThrowException() {
        when(productDetails.getPrice()).thenReturn(BigDecimal.valueOf(20.305));

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.create(productDetails, user));

        verify(productStore, never()).save(any());
        assertEquals("Price should have MAX 2 decimal digits.", exception.getMessage());
    }

    @Test
    void create_butStoreThrowException_thenThrowException() {
        String storeExceptionMessage = "error";
        doThrow(new RuntimeException(storeExceptionMessage)).when(productStore).save(any(Product.class));

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.create(productDetails, user));

        verify(productStore).save(any());
        assertEquals("Failed to save product for reason: "+storeExceptionMessage, exception.getMessage());
    }

    @Test
    void delete_thenSuccess() {
        Product result = productService.delete(productId, user);
        assertSame(product, result);
        verify(productStore).remove(productId);
    }

    @Test
    void delete_butRequesterIsNotAdmin_thenThrowException() {
        when(user.getRole()).thenReturn(Role.CUSTOMER);

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.delete(productId, user));
        assertEquals("User should be admin to delete a product.", exception.getMessage());
        verify(productStore, never()).remove(productId);
    }

    @Test
    void delete_butProductDidNotExist_thenThrowException() {
        when(productStore.remove(productId)).thenReturn(null);

        ProductActionException exception = assertThrows(ProductActionException.class, () -> productService.delete(productId, user));
        assertEquals("Product %s does not exist.".formatted(productId), exception.getMessage());
        verify(productStore).remove(productId);
    }

    @Test
    void getById_delegatesToStore() {
        when(productStore.getById(productId)).thenReturn(product);
        Product result = productService.getById(productId);
        verify(productStore).getById(productId);
        assertSame(product, result);
    }

    @Test
    void getAll_delegatesToStore() {
        List<Product> allProducts = List.of(product);
        when(productStore.getAll()).thenReturn(allProducts);
        List<Product> allResult = productService.getAll();
        verify(productStore).getAll();
        assertSame(allProducts, allResult);
    }
}