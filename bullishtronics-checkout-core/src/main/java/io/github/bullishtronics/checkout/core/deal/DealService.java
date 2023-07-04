package io.github.bullishtronics.checkout.core.deal;

import io.github.bullishtronics.checkout.core.deal.exception.DealNotSupportedException;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.user.User;

import java.util.List;

/**
 * CRUD Deals made by admins.
 */
public interface DealService {
    Deal createDeal(DealDetails dealDetails, User requester) throws DealNotSupportedException;
    void cancelDeal(String dealId, User requester);
    List<Deal> getAllActive();
}
