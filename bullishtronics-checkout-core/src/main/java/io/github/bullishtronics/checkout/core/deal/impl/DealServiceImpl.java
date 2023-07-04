package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.exception.DealActionException;
import io.github.bullishtronics.checkout.core.deal.exception.DealNotSupportedException;
import io.github.bullishtronics.checkout.core.deal.DealService;
import io.github.bullishtronics.checkout.core.deal.DealStore;
import io.github.bullishtronics.checkout.models.user.User;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealDetails;

import java.util.List;

import static io.github.bullishtronics.checkout.models.user.Role.ADMIN;

public class DealServiceImpl implements DealService {
    private final DealStore dealStore;
    private final DealFactory dealFactory;

    public DealServiceImpl(DealStore dealStore, DealFactory dealFactory) {
        this.dealStore = dealStore;
        this.dealFactory = dealFactory;
    }

    @Override
    public Deal createDeal(DealDetails dealDetails, User requester) throws DealNotSupportedException {
        if (requester.getRole() == ADMIN) {
            Deal deal = dealFactory.build(dealDetails);
            dealStore.save(deal);
            return deal;
        }

        throw new DealActionException("Only admins can create deals");
    }

    @Override
    public void cancelDeal(String dealId, User requester) {
        if (requester.getRole() == ADMIN) {
            dealStore.delete(dealId);
        }else{
            throw new DealActionException("Only admins can cancel deals");
        }
    }

    @Override
    public List<Deal> getAllActive() {
        return dealStore.getAllActive();
    }
}
