package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.DealService;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.core.deal.exception.DiscountNotExistException;
import io.github.bullishtronics.checkout.core.deal.DiscounterService;
import io.github.bullishtronics.checkout.core.deal.Discounter;
import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.Discount;
import io.github.bullishtronics.checkout.models.deal.DealType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiscounterServiceImpl implements DiscounterService {
    private final DealService dealService;
    private final Map<DealType, Discounter<?>> discountersMap;

    public DiscounterServiceImpl(DealService dealService, Map<DealType, Discounter<?>> discountersMap) {
        this.dealService = dealService;
        this.discountersMap = discountersMap;

    }

    @Override
    public List<Discount> compute(Basket basket) {
        List<Deal> allActiveDeals = this.dealService.getAllActive();

        List<Discount> applicableDiscounts = new ArrayList<>();
        for (Deal activeDeal : allActiveDeals) {
            DealType dealType = activeDeal.getType();
            Discounter<?> discounter = discountersMap.get(dealType);
            if (discounter == null) {
                throw new DiscountNotExistException("No discounter for deal type " + dealType);
            }

            try {
                Discount discount = discounter.computeDeal(basket, activeDeal);
                if (discount != null && discount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    applicableDiscounts.add(discount);
                }
            } catch (Exception e) {
                throw new DiscountActionException("Can't compute discount for deal %s for the following reason: %s".formatted(activeDeal.getDealId(), e.getMessage()));
            }
        }

        return getBestDiscountPossible(applicableDiscounts);
    }

    private List<Discount> getBestDiscountPossible(List<Discount> applicableDiscounts) {
        List<List<Discount>> candidatesForResult = new ArrayList<>();
        List<Discount> stackableDiscounts = new ArrayList<>();
        for (Discount applicableDiscount : applicableDiscounts) {
            if (applicableDiscount.isStackable()) {
                stackableDiscounts.add(applicableDiscount);
            } else {
                candidatesForResult.add(List.of(applicableDiscount));
            }
        }

        candidatesForResult.add(stackableDiscounts);

        Optional<List<Discount>> optionalBestDiscount = candidatesForResult.stream()
                .max(Comparator.comparing(list -> list.stream()
                        .map(Discount::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)));

        return optionalBestDiscount.orElse(List.of());
    }
}
