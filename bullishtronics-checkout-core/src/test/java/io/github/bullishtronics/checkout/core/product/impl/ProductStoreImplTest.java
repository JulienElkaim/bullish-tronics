package io.github.bullishtronics.checkout.core.product.impl;

import io.github.bullishtronics.checkout.core.product.ProductStore;
import io.github.bullishtronics.checkout.core.product.exception.ProductActionException;
import io.github.bullishtronics.checkout.models.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductStoreImplTest {
    private final String productId = "productId";
    private final String productId2 = "productId2";
    private final Product product = mock(Product.class);
    private final Product product2 = mock(Product.class);
    private ProductStore productStore;

    @BeforeEach
    void init() {
        productStore = new ProductStoreImpl(Map.of(productId, product));
        when(product.getProductId()).thenReturn(productId);
        when(product2.getProductId()).thenReturn(productId2);
    }

    @Test
    void save_whenNewRecord_saveRecordSuccessfully(){
        assertNull(productStore.getById(productId2));

        productStore.save(product2);

        assertSame(product2, productStore.getById(productId2));
    }

    @Test
    void save_whenRecordAlreadyIn_updateRecord(){
        when(product2.getProductId()).thenReturn(productId);
        assertSame(product, productStore.getById(productId));

        productStore.save(product2);

        assertSame(product2, productStore.getById(productId));
    }

    @Test
    void save_whenRecordHasNullProductId_thenThrowException(){
        when(product2.getProductId()).thenReturn(null);
        ProductActionException exception = assertThrows(ProductActionException.class, () -> productStore.save(product2));
        assertEquals("Can't persist a product with ID null", exception.getMessage());
    }

    @Test
    void getById_returnsProduct(){
        assertSame(product, productStore.getById(productId));
    }

    @Test
    void getById_butRecordNotPresent_returnsNull(){
        assertNull(productStore.getById(productId2));
    }

    @Test
    void getAll_returnListWithProductInside(){
        List<Product> result = productStore.getAll();
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));

        reInitStore(Map.of(productId, product, productId2, product2));
        List<Product> result2 = productStore.getAll();
        assertEquals(2, result2.size());
        assertTrue(result2.contains(product));
        assertTrue(result2.contains(product2));
    }

    @Test
    void remove_removesRecordFromCache() {
        assertSame(product, productStore.getById(productId));

        Product result = productStore.remove(productId);

        assertSame(product, result);
        assertNull(productStore.getById(productId));
    }

    @Test
    void remove_butNotInitiallyIncache_returnsNull() {
        assertNull(productStore.getById(productId2));

        Product result = productStore.remove(productId2);

        assertNull(result);
        assertNull(productStore.getById(productId2));
    }


    private void reInitStore(Map<String, Product> initCache) {
        productStore = new ProductStoreImpl(initCache);
    }


}