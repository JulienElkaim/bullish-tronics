package io.github.bullishtronics.checkout.core.deal;

import io.github.bullishtronics.checkout.core.deal.exception.DiscountActionException;
import io.github.bullishtronics.checkout.models.basket.Basket;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealType;
import io.github.bullishtronics.checkout.models.deal.Discount;

/**
 * Prend en entrée un panier et tous les DEALS associé a son type de dsicount.
 * Retourne les discounts associés au panier.
 *
 * Il se fait appeler par le discount service, qui va etre appeler par le purchase Service
 *
 *
 *
 */
public abstract class Discounter<T extends  Deal> {
    public Discount computeDeal(Basket basket, Deal deal){
        Class<T> supportedDealClass = this.getSupportedDealClass();
        if(!supportedDealClass.isAssignableFrom(deal.getClass())){
            throw new DiscountActionException("Wrong deal processing. Discounter expected a %s object but got %s".formatted(supportedDealClass.getName(), deal.getClass().getName()));
        }
        return compute(basket, supportedDealClass.cast(deal));
    }

    public abstract DealType getApplicableDealType();

    protected abstract Discount compute(Basket basket, T deal);

    protected abstract Class<T> getSupportedDealClass();

}
