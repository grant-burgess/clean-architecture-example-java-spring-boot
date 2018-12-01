package com.grantburgess.usecases.get.offers;

import com.grantburgess.database.inmemory.InMemoryDatabase;
import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.Database;
import com.grantburgess.usecases.Clock;
import com.grantburgess.usecases.get.OfferResponse;
import com.grantburgess.usecases.testdoubles.ClockStub;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class GetOffersTest {
    private static final String OFFER_DESCRIPTION = "description";
    private static final LocalDate OFFER_START_DATE = LocalDate.of(2018, 01, 01);
    private static final LocalDate OFFER_END_DATE = LocalDate.of(2018, 01, 31);
    private static final BigDecimal OFFER_PRICE_AMOUNT = BigDecimal.TEN;
    private static final String OFFER_PRICE_CURRENCY = "GBP";
    private static final Offer.Money OFFER_PRICE = new Offer.Money(OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT);
    private static final OfferResponse.Status OFFER_STATUS = OfferResponse.Status.ACTIVE;
    private static final LocalDate CURRENT_DATE = LocalDate.of(2018, 01, 30);

    private Database database;
    private Clock clock;
    private GetOffers useCase;

    private void assertOffer(
            List<OfferResponse> offers,
            UUID id,
            String name,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            String currency,
            BigDecimal amount,
            OfferResponse.Status status) {
        assertThat(offers,
                hasItem(both(hasProperty("id", equalTo(id)))
                        .and(hasProperty("name", equalTo(name)))
                        .and(hasProperty("description", equalTo(description)))
                        .and(hasProperty("startDate", equalTo(startDate)))
                        .and(hasProperty("endDate", equalTo(endDate)))
                        .and(hasProperty("currency", equalTo(currency)))
                        .and(hasProperty("amount", is(closeTo(amount, BigDecimal.ZERO))))
                        .and(hasProperty("status", equalTo(status)))
                )
        );
    }

    private UUID addOffer(String name, LocalDate endDate) {
        return database.offerGateway().add(new Offer(name, GetOffersTest.OFFER_DESCRIPTION, GetOffersTest.OFFER_START_DATE, endDate, GetOffersTest.OFFER_PRICE));
    }

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        clock = new ClockStub(CURRENT_DATE);
        useCase = new GetOffers(database.offerGateway(), clock);
    }

    @Test
    public void no_offers_returns_empty_response_list() {
        // WHEN
        OffersResponse offersResponse = useCase.execute(new GetOffersRequest());
        // THEN
        assertThat(offersResponse, is(not(nullValue())));
        assertThat(offersResponse.getOffers(), hasSize(0));
    }

    @Test
    public void can_get_offers() {
        // GIVEN
        UUID offer1Id = addOffer("offer-1", OFFER_END_DATE);
        UUID offer2Id = addOffer("offer-2", OFFER_END_DATE);
        // WHEN
        OffersResponse offersResponse = useCase.execute(new GetOffersRequest());
        // THEN
        assertThat(offersResponse, is(not(nullValue())));
        assertThat(offersResponse.getOffers(), hasSize(2));
        assertOffer(offersResponse.getOffers(), offer1Id, "offer-1", OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OFFER_STATUS);
        assertOffer(offersResponse.getOffers(), offer2Id, "offer-2", OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OFFER_STATUS);
    }

    @Test
    public void expired_offers_have_the_status_expired() {
        // GIVEN
        LocalDate expiredOfferEndDate = OFFER_END_DATE.minusDays(2);
        UUID offer1Id = addOffer("offer-1", OFFER_END_DATE);
        UUID offer2Id = addOffer("offer-2", expiredOfferEndDate);
        // WHEN
        OffersResponse offersResponse = useCase.execute(new GetOffersRequest());
        // THEN
        assertThat(offersResponse, is(not(nullValue())));
        assertThat(offersResponse.getOffers(), hasSize(2));
        assertOffer(offersResponse.getOffers(), offer1Id, "offer-1", OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OFFER_STATUS);
        assertOffer(offersResponse.getOffers(), offer2Id, "offer-2", OFFER_DESCRIPTION, OFFER_START_DATE, expiredOfferEndDate, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OfferResponse.Status.EXPIRED);
    }

    @Test
    public void cancelled_offers_do_not_appear_in_list() {
        // GIVEN
        UUID offer1Id = addOffer("offer-1", OFFER_END_DATE);
        UUID offer2Id = addOffer("offer-2", OFFER_END_DATE);
        Offer offer2 = database.offerGateway().getByIdExcludingCancelled(offer2Id);
        offer2.cancel(CURRENT_DATE);
        database.offerGateway().update(offer2);
        // WHEN
        OffersResponse offersResponse = useCase.execute(new GetOffersRequest());
        // THEN
        assertThat(offersResponse, is(not(nullValue())));
        assertThat(offersResponse.getOffers(), hasSize(1));
        assertOffer(offersResponse.getOffers(), offer1Id, "offer-1", OFFER_DESCRIPTION, OFFER_START_DATE, OFFER_END_DATE, OFFER_PRICE_CURRENCY, OFFER_PRICE_AMOUNT, OFFER_STATUS);
    }
}