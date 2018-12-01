package com.grantburgess.usecases.canceloffer;

import com.grantburgess.database.inmemory.InMemoryDatabase;
import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.Database;
import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.usecases.testdoubles.ClockStub;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CancelOfferTest {
    private static final LocalDate OFFER_START_DATE = LocalDate.of(2018, 1, 1);
    private static final LocalDate OFFER_END_DATE = LocalDate.of(2018, 1, 31);
    private static final LocalDate CURRENT_DATE = OFFER_START_DATE.plusDays(14);

    private Offer offer;
    private Database database;
    private CancelOffer useCase;

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        useCase = new CancelOffer(database.offerGateway(), new ClockStub(CURRENT_DATE));
        offer = new Offer(
                "name",
                "description",
                OFFER_START_DATE,
                OFFER_END_DATE,
                new Offer.Money("GBP", BigDecimal.TEN)
        );
    }

    @Test(expected = OfferGateway.OfferNotFoundException.class)
    public void cannot_cancel_offer_that_does_not_exist() {
        useCase.execute(new CancelOfferRequest(UUID.randomUUID()));
    }

    @Test(expected = OfferGateway.OfferNotFoundException.class)
    public void cannot_cancel_offer_that_is_already_cancelled() {
        UUID offerId = database.offerGateway().add(offer);
        Offer offer2 = database.offerGateway().getByIdExcludingCancelled(offerId);
        offer2.cancel(CURRENT_DATE);
        database.offerGateway().update(offer2);

        useCase.execute(new CancelOfferRequest(UUID.randomUUID()));
    }

    @Test(expected = OfferGateway.CannotCancelOfferThatHasExpiredException.class)
    public void cannot_cancel_offer_after_it_has_expired() {
        // GIVEN
        UUID offerId = database.offerGateway().add(offer);
        LocalDate currentClockDate = OFFER_END_DATE.plusDays(1);
        useCase = new CancelOffer(database.offerGateway(), new ClockStub(currentClockDate));

        // WHEN
        useCase.execute(new CancelOfferRequest(offerId));
    }

    @Test
    public void can_cancel_offer() {
        // GIVEN
        UUID offerId = database.offerGateway().add(offer);

        // WHEN
        useCase.execute(new CancelOfferRequest(offerId));

        // THEN
        Offer cancelledOffer = database.offerGateway().getByIdExcludingCancelled(offerId);
        assertThat(cancelledOffer, is(nullValue()));
    }
}