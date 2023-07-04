package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.deal.DealStore;
import io.github.bullishtronics.checkout.core.deal.exception.DealActionException;
import io.github.bullishtronics.checkout.core.deal.exception.DealNotSupportedException;
import io.github.bullishtronics.checkout.models.deal.Deal;
import io.github.bullishtronics.checkout.models.deal.DealDetails;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DealServiceImplTest {
    private final DealStore dealStore = mock(DealStore.class);
    private final DealFactory dealFactory = mock(DealFactory.class);
    private final DealDetails dealDetails = mock(DealDetails.class);
    private final Deal deal = mock(Deal.class);
    private final User requester = mock(User.class);
    private final String dealId = "dealId";

    private DealServiceImpl dealService;

    @BeforeEach
    void init() throws DealNotSupportedException {
        when(requester.getRole()).thenReturn(Role.ADMIN);

        dealService = new DealServiceImpl(dealStore, dealFactory);
        when(dealFactory.build(dealDetails)).thenReturn(deal);
        when(dealStore.getAllActive()).thenReturn(List.of(deal));
    }

    @Test
    void createDeal() throws DealNotSupportedException {
        Deal dealCreated = dealService.createDeal(dealDetails, requester);
        assertSame(deal, dealCreated);
        verify(dealFactory).build(dealDetails);
        verify(dealStore).save(deal);
    }

    @Test
    void createDeal_whenRequesterIsNotAdmin() throws DealNotSupportedException {
        when(requester.getRole()).thenReturn(Role.CUSTOMER);

        DealActionException exception = assertThrows(DealActionException.class, () -> dealService.createDeal(dealDetails, requester));

        assertEquals("Only admins can create deals", exception.getMessage());
        verify(dealFactory, never()).build(any());
        verify(dealStore, never()).save(any());
    }

    @Test
    void createDeal_whenDealFactoryThrowDealNotSupportedException_passThroughWithNoModification() throws DealNotSupportedException {
        when(dealFactory.build(dealDetails)).thenThrow(new DealNotSupportedException("Deal not supported"));

        DealNotSupportedException exception = assertThrows(DealNotSupportedException.class, () -> dealService.createDeal(dealDetails, requester));

        assertEquals("Deal not supported", exception.getMessage());
        verify(dealFactory).build(dealDetails);
        verify(dealStore, never()).save(any());
    }

    @Test
    void cancelDeal() {
        dealService.cancelDeal(dealId, requester);
        verify(dealStore).delete(dealId);
    }

    @Test
    void cancelDeal_whenRequesterIsNotAdmin() {
        when(requester.getRole()).thenReturn(Role.CUSTOMER);
        DealActionException exception = assertThrows(DealActionException.class, () -> dealService.cancelDeal(dealId, requester));

        assertEquals("Only admins can cancel deals", exception.getMessage());
        verify(dealStore, never()).delete(any());
    }

    @Test
    void getAllActive_isPassThroughToStore(){
        List<Deal> allActive = dealService.getAllActive();

        assertEquals(1, allActive.size());
        assertSame(deal, allActive.get(0));
        verify(dealStore).getAllActive();
    }
}