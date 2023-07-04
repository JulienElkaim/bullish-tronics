package io.github.bullishtronics.checkout.app.web;


import io.github.bullishtronics.checkout.app.security.BasicAuthBased;
import io.github.bullishtronics.checkout.core.purchase.PurchaseService;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.data.adater.OrderDtoAdapter;
import io.github.bullishtronics.checkout.data.adater.QuotationDtoAdapter;
import io.github.bullishtronics.checkout.io.order.OrderDto;
import io.github.bullishtronics.checkout.io.order.QuotationDto;
import io.github.bullishtronics.checkout.models.order.Order;
import io.github.bullishtronics.checkout.models.order.Quotation;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@BasicAuthBased
@RequestMapping("api/v1/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final UserService userService;
    private final OrderDtoAdapter orderDtoAdapter;
    private final QuotationDtoAdapter quotationDtoAdapter;

    public PurchaseController(PurchaseService purchaseService,
                              UserService userService,
                              OrderDtoAdapter orderDtoAdapter,
                              QuotationDtoAdapter quotationDtoAdapter) {
        this.purchaseService = purchaseService;
        this.userService = userService;
        this.orderDtoAdapter = orderDtoAdapter;
        this.quotationDtoAdapter = quotationDtoAdapter;
    }

    @PostMapping(value = "/quotation/{basketOwnerId}")
    public ResponseEntity<QuotationDto> quoteBasket(@PathVariable String basketOwnerId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        Quotation quotation = purchaseService.quote(basketOwnerId, requester);
        return ResponseEntity.ok(quotation == null ? null : quotationDtoAdapter.adapte(quotation));
    }

    @PostMapping(value = "/orders/buy")
    public ResponseEntity<OrderDto> placeOrder(@RequestParam String quotationId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        Order orderPlaced = purchaseService.placeOrder(quotationId, requester);
        return ResponseEntity.ok(orderPlaced == null ? null : orderDtoAdapter.adapte(orderPlaced));
    }

    @GetMapping(value = "/orders/myOrders")
    public ResponseEntity<Collection<OrderDto>> myOrders() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        List<OrderDto> myOrders = purchaseService.getMyOrders(requester).stream()
                .map(orderDtoAdapter::adapte).toList();
        return ResponseEntity.ok(myOrders);
    }

    @GetMapping(value = "/orders/{orderId}")
    public ResponseEntity<OrderDto> getById(@PathVariable String orderId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        Order order = purchaseService.getById(orderId, requester);
        return ResponseEntity.ok(order == null ? null : orderDtoAdapter.adapte(order));
    }
}
