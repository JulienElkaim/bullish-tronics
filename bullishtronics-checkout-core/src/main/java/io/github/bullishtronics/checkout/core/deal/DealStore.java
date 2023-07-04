package io.github.bullishtronics.checkout.core.deal;

import io.github.bullishtronics.checkout.models.deal.Deal;

import java.util.List;

public interface DealStore {
    void save(Deal deal);
    void delete(String dealId);
    List<Deal> getAllActive();
}
