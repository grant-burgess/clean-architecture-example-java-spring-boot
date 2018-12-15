package com.grantburgess.ports.usescases.addoffer;

import com.grantburgess.database.inmemory.InMemoryDatabase;
import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.Database;
import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.ports.presenters.OfferCreatedOutputBoundary;
import com.grantburgess.usecases.addoffer.AddOffer;
import com.grantburgess.usecases.testdoubles.ClockStub;
import com.grantburgess.usecases.testdoubles.OfferCreatedPresenterSpy;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class AddOfferTest {
    private static final String OFFER_REQUEST_NAME = "name";
    private static final String OFFER_REQUEST_DESCRIPTION = "description";
    private static final LocalDate OFFER_REQUEST_START_DATE = LocalDate.of(2018, 1, 1);
    private static final LocalDate OFFER_REQUEST_END_DATE = LocalDate.of(2018, 1, 31);
    private static final String OFFER_REQUEST_CURRENCY = "GBP";
    private static final BigDecimal OFFER_REQUEST_AMOUNT = BigDecimal.TEN;
    private static final LocalDate CURRENT_DATE = OFFER_REQUEST_START_DATE.plusDays(14);

    private Database database;
    private AddOfferInputBoundary useCase;
    private AddOfferRequest request;
    private OfferCreatedOutputBoundary presenter;

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        presenter = new OfferCreatedPresenterSpy();
        useCase = new AddOffer(presenter, database.offerGateway(), new ClockStub(CURRENT_DATE));
        request = new AddOfferRequest(OFFER_REQUEST_NAME, OFFER_REQUEST_DESCRIPTION, OFFER_REQUEST_START_DATE, OFFER_REQUEST_END_DATE, OFFER_REQUEST_AMOUNT, OFFER_REQUEST_CURRENCY);
    }

    @Test(expected = OfferGateway.OfferNameAlreadyExistsException.class)
    public void cannot_add_offer_with_same_name() {
        // GIVEN
        database.offerGateway().add(
                new Offer(
                        OFFER_REQUEST_NAME,
                        OFFER_REQUEST_DESCRIPTION,
                        OFFER_REQUEST_START_DATE,
                        OFFER_REQUEST_END_DATE,
                        new Offer.Money(OFFER_REQUEST_CURRENCY, OFFER_REQUEST_AMOUNT)
                )
        );
        // WHEN
        useCase.execute(request);
    }

    @Test(expected = OfferGateway.OfferStartDateGreaterThanEndDateException.class)
    public void offer_start_date_cannot_be_later_than_end_date() {
        // GIVEN
        LocalDate startDate = LocalDate.of(2018, 1, 2);
        LocalDate endDateEarlierThanStartDate = startDate.minusDays(1);
        // WHEN
        useCase.execute(new AddOfferRequest(OFFER_REQUEST_NAME, OFFER_REQUEST_DESCRIPTION, startDate, endDateEarlierThanStartDate, OFFER_REQUEST_AMOUNT, OFFER_REQUEST_CURRENCY));
    }

    @Test(expected = OfferGateway.OfferEndDateCannotBeBeforeCurrentSystemDateException.class)
    public void cannot_add_offer_with_an_end_date_that_is_before_the_current_date() {
        // GIVEN
        LocalDate endDateEarlierThanCurrentDate = CURRENT_DATE.minusDays(1);
        // WHEN
        useCase.execute(new AddOfferRequest(OFFER_REQUEST_NAME, OFFER_REQUEST_DESCRIPTION, OFFER_REQUEST_START_DATE, endDateEarlierThanCurrentDate, OFFER_REQUEST_AMOUNT, OFFER_REQUEST_CURRENCY));
    }

    @Test
    public void can_add_offer() {
        Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        OfferCreatedPresenterSpy presenterSpy = (OfferCreatedPresenterSpy) presenter;
        // WHEN
        useCase.execute(request);
        // THEN
        assertThat(presenterSpy.isOfferPresented(), equalTo(true));
        UUID offerId = presenterSpy.getResponseModel().getId();
        assertThat(uuidPattern.matcher(offerId.toString()).matches(), equalTo(true));
        Collection<Offer> offers = database.offerGateway().getAllExcludingCancelled();
        assertThat(offers, hasSize(1));
        assertThat(offers,
                hasItem(both(hasProperty("id", equalTo(offerId)))
                        .and(hasProperty("name", equalTo(OFFER_REQUEST_NAME)))
                        .and(hasProperty("description", equalTo(OFFER_REQUEST_DESCRIPTION)))
                        .and(hasProperty("startDate", equalTo(OFFER_REQUEST_START_DATE)))
                        .and(hasProperty("endDate", equalTo(OFFER_REQUEST_END_DATE)))
                        .and(hasProperty("price", hasProperty("currency", equalTo("GBP"))))
                        .and(hasProperty("price", hasProperty("amount", is(closeTo(OFFER_REQUEST_AMOUNT, BigDecimal.ZERO)))))
                        .and(hasProperty("cancelDate", is(nullValue())))
                )
        );
    }
}