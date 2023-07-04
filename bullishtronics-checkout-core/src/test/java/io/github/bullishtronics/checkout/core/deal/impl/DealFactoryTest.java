package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.exception.DealNotSupportedException;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDeal;
import io.github.bullishtronics.checkout.models.deal.customs.GlobalDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemCombinationTargetPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.ItemModuloLastPercentDealDetails;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDeal;
import io.github.bullishtronics.checkout.models.deal.customs.UserLocationContainsPercentDealDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.configuration.IMockitoConfiguration;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DealFactoryTest {
    private DealFactory factory;
    private final GlobalDealDetails globalDealDetails = new GlobalDealDetails(
            "tagline",
            Instant.now().minus(Duration.of(3, ChronoUnit.HOURS)),
            Instant.now().plus(Duration.of(3, ChronoUnit.HOURS)),
            10
    );
    private final ItemModuloLastPercentDealDetails imlpDealDetails = new ItemModuloLastPercentDealDetails(
            "productId",
            3,
            10,
            "tagline",
            Instant.now().minus(Duration.of(3, ChronoUnit.HOURS)),
            Instant.now().plus(Duration.of(3, ChronoUnit.HOURS))
    );
    private final ItemCombinationTargetPercentDealDetails ictpDealDetails = new ItemCombinationTargetPercentDealDetails(
            "targetProductId",
            10,
            "tagline",
            Instant.now().minus(Duration.of(3, ChronoUnit.HOURS)),
            Instant.now().plus(Duration.of(3, ChronoUnit.HOURS)),
            Set.of("productId1", "productId2")
    );
    private final UserLocationContainsPercentDealDetails ulcpDealDetails = new UserLocationContainsPercentDealDetails(
            "tagline",
            Instant.now().minus(Duration.of(3, ChronoUnit.HOURS)),
            Instant.now().plus(Duration.of(3, ChronoUnit.HOURS)),
            10,
            "locationSubtext"
    );
    @BeforeEach
    void init(){
        factory = new DealFactory();
    }

    @Test
    void build_globalDeal() throws DealNotSupportedException {
        Deal deal = factory.build(globalDealDetails);
        assertTrue(deal instanceof GlobalDeal);
        GlobalDeal typedDeal = (GlobalDeal) deal;

        assertEquals(globalDealDetails.getDealType(), typedDeal.getType());
        assertEquals(globalDealDetails.getTagline(), typedDeal.getTagline());
        assertEquals(globalDealDetails.getStartTime(), typedDeal.getStartTime());
        assertEquals(globalDealDetails.getEndTime(), typedDeal.getEndTime());
        assertEquals(globalDealDetails.getPercent(), typedDeal.getPercent());
    }

    @Test
    void build_itemModuloLastPercentDeal() throws DealNotSupportedException {
        Deal deal = factory.build(imlpDealDetails);
        assertTrue(deal instanceof ItemModuloLastPercentDeal);
        ItemModuloLastPercentDeal typedDeal = (ItemModuloLastPercentDeal) deal;

        assertEquals(imlpDealDetails.getDealType(), typedDeal.getType());
        assertEquals(imlpDealDetails.getTagline(), typedDeal.getTagline());
        assertEquals(imlpDealDetails.getStartTime(), typedDeal.getStartTime());
        assertEquals(imlpDealDetails.getEndTime(), typedDeal.getEndTime());
        assertEquals(imlpDealDetails.getPercent(), typedDeal.getPercent());
        assertEquals(imlpDealDetails.getProductId(), typedDeal.getProductId());
        assertEquals(imlpDealDetails.getModuloNumber(), typedDeal.getModuloNumber());
    }

    @Test
    void build_itemCombinationTargetPercentDeal() throws DealNotSupportedException {
        Deal deal = factory.build(ictpDealDetails);
        assertTrue(deal instanceof ItemCombinationTargetPercentDeal);
        ItemCombinationTargetPercentDeal typedDeal = (ItemCombinationTargetPercentDeal) deal;

        assertEquals(ictpDealDetails.getDealType(), typedDeal.getType());
        assertEquals(ictpDealDetails.getTagline(), typedDeal.getTagline());
        assertEquals(ictpDealDetails.getStartTime(), typedDeal.getStartTime());
        assertEquals(ictpDealDetails.getEndTime(), typedDeal.getEndTime());
        assertEquals(ictpDealDetails.getPercent(), typedDeal.getPercent());
        assertEquals(ictpDealDetails.getTargetProductId(), typedDeal.getTargetProductId());
        assertEquals(ictpDealDetails.getProductIdsToCombine(), typedDeal.getProductIdsToCombine());
    }

    @Test
    void build_userLocationContainsPercentDeal() throws DealNotSupportedException {
        Deal deal = factory.build(ulcpDealDetails);
        assertTrue(deal instanceof UserLocationContainsPercentDeal);
        UserLocationContainsPercentDeal typedDeal = (UserLocationContainsPercentDeal) deal;

        assertEquals(ulcpDealDetails.getDealType(), typedDeal.getType());
        assertEquals(ulcpDealDetails.getTagline(), typedDeal.getTagline());
        assertEquals(ulcpDealDetails.getStartTime(), typedDeal.getStartTime());
        assertEquals(ulcpDealDetails.getEndTime(), typedDeal.getEndTime());
        assertEquals(ulcpDealDetails.getPercent(), typedDeal.getPercent());
        assertEquals(ulcpDealDetails.getLocationSubtext(), typedDeal.getLocationSubtext());
    }

    @Test
    void build_unsupportedDetailsType() {
        DealDetails mockedDetails = mock(DealDetails.class);
        DealNotSupportedException exception = assertThrows(DealNotSupportedException.class, () -> factory.build(mockedDetails));
        assertEquals("We do not support this DealDetails yet: " + mockedDetails.getClass().getName(), exception.getMessage());
    }




}