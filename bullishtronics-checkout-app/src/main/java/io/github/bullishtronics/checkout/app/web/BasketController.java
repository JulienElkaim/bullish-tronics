package io.github.bullishtronics.checkout.app.web;


import io.github.bullishtronics.checkout.app.security.BasicAuthBased;
import io.github.bullishtronics.checkout.app.security.OnlyAdminEndpoint;
import io.github.bullishtronics.checkout.core.basket.BasketService;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.data.adater.BasketDtoAdapter;
import io.github.bullishtronics.checkout.io.basket.BasketDto;
import io.github.bullishtronics.checkout.io.basket.BasketModificationReport;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasicAuthBased
@RequestMapping("api/v1/basket")
public class BasketController {
    private final BasketService basketService;
    private final BasketDtoAdapter basketDtoAdapter;
    private final UserService userService;

    public BasketController(BasketService basketService,
                            BasketDtoAdapter basketDtoAdapter,
                            UserService userService) {
        this.basketService = basketService;
        this.basketDtoAdapter = basketDtoAdapter;
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<BasketDto> getMyBasket() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        Basket basket = basketService.getBasket(requester.getUsername(), requester);
        return ResponseEntity.ok(basket == null ? null : basketDtoAdapter.adapte(basket));
    }

    @OnlyAdminEndpoint
    @GetMapping(value = "/{ownerId}")
    public ResponseEntity<BasketDto> getBasketByUserId(@PathVariable String ownerId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        Basket basket = basketService.getBasket(ownerId, requester);
        return ResponseEntity.ok(basket == null ? null : basketDtoAdapter.adapte(basket));
    }

    @PutMapping(value = "/{ownerId}/removeProduct")
    public ResponseEntity<BasketModificationReport> removeProduct(@PathVariable String ownerId,
                                                                  @RequestParam String productId,
                                                                  @RequestParam Integer quantity) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        try {
            Basket basket = basketService.removeProduct(productId, quantity, ownerId, requester);
            return ResponseEntity.ok(BasketModificationReport.success(basketDtoAdapter.adapte(basket)));
        } catch (Exception e) {
            return ResponseEntity.ok(BasketModificationReport.failure(e.getMessage()));
        }
    }

    @PutMapping(value = "/{ownerId}/addProduct")
    public ResponseEntity<BasketModificationReport> addProduct(@PathVariable String ownerId,
                                                               @RequestParam String productId,
                                                               @RequestParam Integer quantity) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        try {
            Basket basket = basketService.addProduct(productId, quantity, ownerId, requester);
            return ResponseEntity.ok(BasketModificationReport.success(basketDtoAdapter.adapte(basket)));
        } catch (Exception e) {
            return ResponseEntity.ok(BasketModificationReport.failure(e.getMessage()));
        }
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<?> clearMyBasket() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        basketService.clear(requester.getUsername(), requester);
        return ResponseEntity.ok().build();
    }
}
