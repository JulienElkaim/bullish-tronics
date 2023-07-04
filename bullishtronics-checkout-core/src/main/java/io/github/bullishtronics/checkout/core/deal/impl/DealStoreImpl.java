package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.DealStore;
import io.github.bullishtronics.checkout.core.product.exception.ProductActionException;
import io.github.bullishtronics.checkout.models.deal.Deal;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DealStoreImpl implements DealStore {
    private final ConcurrentHashMap<String, Deal> dealsMap;

    public DealStoreImpl(Map<String, Deal> dealsMap) {
        this.dealsMap = new ConcurrentHashMap<>(dealsMap);
    }

    @Override
    public void save(Deal deal) {
        if (deal.getDealId() == null) {
            throw new ProductActionException("Can't persist a deal with ID null.");
        }
        this.dealsMap.put(deal.getDealId(), deal);
    }

    @Override
    public void delete(String dealId) {
        this.dealsMap.remove(dealId);
    }

    @Override
    public List<Deal> getAllActive() {
        Instant now = Instant.now();
        return dealsMap.values().stream()
                .filter(deal -> now.isBefore(deal.getEndTime()) && now.isAfter(deal.getStartTime()))
                .collect(Collectors.toList());
    }

    public Deal getById(String dealId){
        return dealsMap.get(dealId);
    }
}
