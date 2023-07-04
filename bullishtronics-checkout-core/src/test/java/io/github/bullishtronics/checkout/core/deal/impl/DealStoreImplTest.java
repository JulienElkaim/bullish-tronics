package io.github.bullishtronics.checkout.core.deal.impl;

import io.github.bullishtronics.checkout.core.product.exception.ProductActionException;
import io.github.bullishtronics.checkout.models.deal.Deal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DealStoreImplTest {
    private final Deal deal1 = mock(Deal.class);
    private final Deal deal2 = mock(Deal.class);
    private final String dealId1 = "dealId1";
    private final String dealId2 = "dealId2";

    private DealStoreImpl dealStore;
    @BeforeEach
    void init(){
        reInit(Map.of(dealId1, deal1));

        when(deal1.getDealId()).thenReturn(dealId1);
        when(deal2.getDealId()).thenReturn(dealId2);
        when(deal1.getStartTime()).thenReturn(Instant.now().minusSeconds(100));
        when(deal1.getEndTime()).thenReturn(Instant.now().plusSeconds(100));
        when(deal2.getStartTime()).thenReturn(Instant.now().plusSeconds(100));
        when(deal2.getEndTime()).thenReturn(Instant.now().plusSeconds(1000));
    }

    @Test
    void save_dontThrow(){
        assertNull(dealStore.getById(dealId2));
        dealStore.save(deal2);
        assertSame(deal2, dealStore.getById(dealId2));
    }

    @Test
    void save_whenDealIdIsNull_thenThrow(){
        when(deal2.getDealId()).thenReturn(null);
        ProductActionException exception = assertThrows(ProductActionException.class, () -> dealStore.save(deal2));
        assertEquals("Can't persist a deal with ID null.", exception.getMessage());
    }

    @Test
    void delete(){
        assertSame(deal1, dealStore.getById(dealId1));
        dealStore.delete(dealId1);
        assertNull(dealStore.getById(dealId1));
    }

    @Test
    void getById(){
        assertSame(deal1, dealStore.getById(dealId1));
    }

    @Test
    void getById_returnsNullWhenIdNotStored(){
        assertNull(dealStore.getById(dealId2));
    }

    @Test
    void getAllActive_onlyOneActive(){
        reInit(Map.of(dealId1, deal1, dealId2, deal2));
        List<Deal> result = dealStore.getAllActive();
        assertEquals(1, result.size());
        assertSame(deal1, result.get(0));
    }

    @Test
    void getAllActive_bothAreActive(){
        reInit(Map.of(dealId1, deal1, dealId2, deal2));
        when(deal2.getStartTime()).thenReturn(Instant.now().minusSeconds(100));

        List<Deal> result = dealStore.getAllActive();
        assertEquals(2, result.size());
        assertTrue(result.contains(deal1));
        assertTrue(result.contains(deal2));
    }

    @Test
    void getAllActive_noneAreActive(){
        reInit(Map.of(dealId1, deal1, dealId2, deal2));
        when(deal1.getStartTime()).thenReturn(Instant.now().plusSeconds(100));

        List<Deal> result = dealStore.getAllActive();
        assertTrue(result.isEmpty());
    }

    @Test
    private void reInit(Map<String, Deal> initCache){
        dealStore = new DealStoreImpl(initCache);
    }

}