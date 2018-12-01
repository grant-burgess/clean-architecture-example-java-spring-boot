package com.grantburgess.presenters;

import com.grantburgess.usecases.get.OfferResponse;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class OfferPresenterTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final LocalDate START_DATE = LocalDate.of(2018, 01, 01);
    private static final LocalDate END_DATE = LocalDate.of(2018, 01, 31);
    private static final String CURRENCY = "GBP";
    private static final BigDecimal AMOUNT = BigDecimal.TEN;
    private static final OfferResponse.Status STATUS = OfferResponse.Status.ACTIVE;

    @Test
    public void can_present_offer() {
        OfferPresenter presenter = new OfferPresenter();
        // WHEN
        presenter.present(new OfferResponse(ID, NAME, DESCRIPTION, START_DATE, END_DATE, CURRENCY, AMOUNT, STATUS));

        // THEN
        OfferViewModel viewModel = presenter.getViewModel();
        assertThat(viewModel, is(not(nullValue())));
        assertThat(viewModel,
                both(hasProperty("id", equalTo(ID.toString())))
                        .and(hasProperty("name", equalTo(NAME)))
                        .and(hasProperty("description", equalTo(DESCRIPTION)))
                        .and(hasProperty("duration", hasProperty("startDate", equalTo("2018-01-01"))))
                        .and(hasProperty("duration", hasProperty("endDate", equalTo("2018-01-31"))))
                        .and(hasProperty("currency", equalTo("GBP")))
                        .and(hasProperty("price", equalTo("10.00")))
                        .and(hasProperty("status", equalTo(STATUS.name())))
        );
    }
}