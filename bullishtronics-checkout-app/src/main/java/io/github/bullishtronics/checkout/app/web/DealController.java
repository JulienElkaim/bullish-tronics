package io.github.bullishtronics.checkout.app.web;


import io.github.bullishtronics.checkout.app.security.BasicAuthBased;
import io.github.bullishtronics.checkout.app.security.OnlyAdminEndpoint;
import io.github.bullishtronics.checkout.core.deal.DealService;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.data.adater.DealDtoAdapter;
import io.github.bullishtronics.checkout.io.deal.CreateDealReport;
import io.github.bullishtronics.checkout.io.deal.DealDto;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealDetails;
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
@RequestMapping("api/v1/deals")
public class DealController {
    private final DealService dealService;
    private final DealDtoAdapter dealDtoAdapter;
    private final UserService userService;

    public DealController(DealService dealService,
                          DealDtoAdapter dealDtoAdapter,
                          UserService userService) {
        this.dealService = dealService;
        this.dealDtoAdapter = dealDtoAdapter;
        this.userService = userService;
    }

    @PostMapping(value = "/new")
    @OnlyAdminEndpoint
    public ResponseEntity<CreateDealReport> createDeal(@RequestBody DealDetails details) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        try {
            Deal deal = dealService.createDeal(details, requester);
            return ResponseEntity.ok(CreateDealReport.success(deal.getDealId()));
        } catch (Exception e) {
            return ResponseEntity.ok(CreateDealReport.failure(e.getMessage()));
        }
    }

    @DeleteMapping(value = "/{dealId}")
    @OnlyAdminEndpoint
    public ResponseEntity<Void> cancelDeal(@PathVariable String dealId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        dealService.cancelDeal(dealId, requester);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/")
    public ResponseEntity<Collection<DealDto>> getAllActive() {
        List<DealDto> deals = dealService.getAllActive().stream()
                .map(dealDtoAdapter::adapte)
                .toList();

        return ResponseEntity.ok(deals);
    }
}

