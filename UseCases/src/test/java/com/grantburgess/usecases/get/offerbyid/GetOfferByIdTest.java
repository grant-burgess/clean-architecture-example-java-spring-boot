package com.grantburgess.usecases.get.offerbyid;

import com.grantburgess.database.inmemory.InMemoryDatabase;
import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.Database;
import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.usecases.Clock;
import com.grantburgess.usecases.get.OfferResponse;
import com.grantburgess.usecases.testdoubles.ClockStub;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class GetOfferByIdTest {
    private static final String OFFER_NAME = "offer-1";
    private static final String OFFER_DESCRIPTION = "description";
    private static final LocalDate OFFER_START_DATE = LocalDate.of(2018, 01, 01);
    private static final LocalDate OFFER_END_DATE = LocalDate.of(2018, 01, 31);
    private static final BigDecimal OFFER_PRICE_AMOUNT = BigDecimal.TEN;
    private static final String OFFER_PRICE_CURRENCY = "GBP";
    private static final Offer.Money OFFER_PRICE = new Offer.Money(OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT);
    private static final LocalDate CURRENT_DATE = LocalDate.of(2018, 01, 30);

    private Database database;
    private Clock clock;
    private GetOfferById useCase;

    private void assertOffer(
            OfferResponse offerResponse,
            UUID id,
            String name,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            String priceCurrency,
            BigDecimal priceAmount,
            OfferResponse.Status status) {
        assertThat(offerResponse,
                both(hasProperty("id", equalTo(id)))
                        .and(hasProperty("name", equalTo(name)))
                        .and(hasProperty("description", equalTo(description)))
                        .and(hasProperty("startDate", equalTo(startDate)))
                        .and(hasProperty("endDate", equalTo(endDate)))
                        .and(hasProperty("currency", equalTo(priceCurrency)))
                        .and(hasProperty("amount", is(closeTo(priceAmount, BigDecimal.ZERO))))
                        .and(hasProperty("status", equalTo(status)))
        );
    }

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        clock = new ClockStub(CURRENT_DATE);
        useCase = new GetOfferById(database.offerGateway(), clock);
    }

    @Test(expected = OfferGateway.OfferNotFoundException.class)
    public void cannot_get_offer_that_does_not_exist() {
        useCase.execute(GetOfferRequest.builder().id(UUID.randomUUID()).build());
    }

    @Test
    public void can_get_expired_offer_by_id() {
        // GIVEN
        LocalDate expiredEndDate = OFFER_END_DATE.minusDays(2);
        UUID offerId = database.offerGateway().add(new Offer(OFFER_NAME, OFFER_DESCRIPTION, OFFER_START_DATE, expiredEndDate, OFFER_PRICE));

        // WHEN
        OfferResponse offerResponse = useCase.execute(GetOfferRequest.builder().id(offerId).build());

        // THEN
        assertThat(offerResponse, is(not(nullValue())));
        assertOffer(offerResponse, offerId, OFFER_NAME, OFFER_DESCRIPTION, OFFER_START_DATE, expiredEndDate, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OfferResponse.Status.EXPIRED);
    }

    @Test(expected = OfferGateway.OfferNotFoundException.class)
    public void cannot_get_cancelled_offer() {
        // GIVEN
        UUID offerId = database.offerGateway().add(new Offer(OFFER_NAME, OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE));
        Offer offer = database.offerGateway().getByIdExcludingCancelled(offerId);
        offer.cancel(CURRENT_DATE);
        database.offerGateway().update(offer);

        // WHEN
        useCase.execute(GetOfferRequest.builder().id(offerId).build());
    }

    @Test
    public void can_get_offer_by_id() {
        // GIVEN
        UUID offerId = database.offerGateway().add(new Offer(OFFER_NAME, OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE));

        // WHEN
        OfferResponse offerResponse = useCase.execute(GetOfferRequest.builder().id(offerId).build());

        // THEN
        assertThat(offerResponse, is(not(nullValue())));
        assertOffer(offerResponse, offerId, OFFER_NAME, OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OfferResponse.Status.ACTIVE);
    }
}