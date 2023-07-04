package io.github.bullishtronics.checkout.io.basket;

public class BasketModificationReport {
    private final boolean isSuccess;
    private final BasketDto newBasketState;
    private final String details;

    public static BasketModificationReport success(BasketDto newBasketState) {
        return new BasketModificationReport(true, newBasketState, null);
    }

    public static BasketModificationReport failure(String details) {
        return new BasketModificationReport(false, null, details);
    }

    private BasketModificationReport(boolean isSuccess, BasketDto newBasketState, String details) {
        this.isSuccess = isSuccess;
        this.newBasketState = newBasketState;
        this.details = details;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public BasketDto getNewBasketState() {
        return newBasketState;
    }

    public String getDetails() {
        return details;
    }
}
