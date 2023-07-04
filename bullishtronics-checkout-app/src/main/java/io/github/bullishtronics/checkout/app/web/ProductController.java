package io.github.bullishtronics.checkout.app.web;

import io.github.bullishtronics.checkout.app.security.BasicAuthBased;
import io.github.bullishtronics.checkout.app.security.OnlyAdminEndpoint;
import io.github.bullishtronics.checkout.core.product.ProductService;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.data.adater.ProductDtoAdapter;
import io.github.bullishtronics.checkout.io.product.CreateProductReport;
import io.github.bullishtronics.checkout.io.product.DeleteProductReport;
import io.github.bullishtronics.checkout.io.product.ProductDto;
import io.github.bullishtronics.checkout.models.product.CreateProductDetails;
import io.github.bullishtronics.checkout.models.product.Product;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@BasicAuthBased
@RequestMapping("api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private final ProductDtoAdapter productDtoAdapter;

    public ProductController(ProductService productService,
                             UserService userService,
                             ProductDtoAdapter productDtoAdapter) {
        this.productService = productService;
        this.userService = userService;
        this.productDtoAdapter = productDtoAdapter;
    }

    @GetMapping(value = "/")
    public ResponseEntity<Collection<ProductDto>> getAll() {
        List<ProductDto> productDtos = productService.getAll().stream()
                .map(productDtoAdapter::adapte)
                .toList();
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable String id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product == null ? null : this.productDtoAdapter.adapte(product));
    }

    @OnlyAdminEndpoint
    @PostMapping(value = "/create")
    public ResponseEntity<CreateProductReport> create(@RequestBody CreateProductDetails details) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        try {
            Product product = productService.create(details, requester);
            return ResponseEntity.ok(CreateProductReport.success(product.getProductId()));
        } catch (Exception e) {
            return ResponseEntity.ok(CreateProductReport.failure(e.getMessage()));
        }
    }

    @OnlyAdminEndpoint
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<DeleteProductReport> delete(@PathVariable String id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        try {
            Product deleted = productService.delete(id, requester);
            return ResponseEntity.ok(DeleteProductReport.success(deleted.getProductId()));
        } catch (Exception e) {
            return ResponseEntity.ok(DeleteProductReport.failure(id, e.getMessage()));
        }
    }
}
