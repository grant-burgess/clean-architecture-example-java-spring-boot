package com.grantburgess.database.jpa.repositories;

import com.grantburgess.database.jpa.TestConfiguration;
import com.grantburgess.database.jpa.data.OfferData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class OfferRepositoryTest {
    private OfferData CANCELLED_OFFER_01;
    private static final UUID CANCELLED_OFFER_ID = UUID.randomUUID();
    private static final String CANCELLED_OFFER_NAME = "cancelled offer name";
    private static final String CANCELLED_OFFER_DESCRIPTION = "cancelled offer description";
    private static final String CANCELLED_OFFER_CURRENCY = "GBP";
    private static final BigDecimal CANCELLED_OFFER_AMOUNT = BigDecimal.TEN;
    private static final LocalDate CANCELLED_OFFER_START_DATE = LocalDate.of(2018, 01, 01);
    private static final LocalDate CANCELLED_OFFER_END_DATE = LocalDate.of(2018, 01, 31);
    private static final LocalDate CANCELLED_OFFER_CANCELLED_DATE = LocalDate.of(2018, 01, 29);

    private OfferData OFFER_01;
    private final UUID OFFER_ID_01 = UUID.randomUUID();
    private static final String OFFER_NAME_01 = "offer name";
    private static final String OFFER_DESCRIPTION_01 = "description";
    private static final String OFFER_CURRENCY_01 = "GBP";
    private static final BigDecimal OFFER_AMOUNT_01 = BigDecimal.TEN;
    private static final LocalDate OFFER_START_DATE_01 = LocalDate.of(2018, 01, 01);
    private static final LocalDate OFFER_END_DATE_01 = LocalDate.of(2018, 01, 31);

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OfferRepository offerRepository;

    @Before
    public void setUp() {
        CANCELLED_OFFER_01 = OfferData
                .builder()
                .id(CANCELLED_OFFER_ID)
                .name(CANCELLED_OFFER_NAME)
                .description(CANCELLED_OFFER_DESCRIPTION)
                .currency(CANCELLED_OFFER_CURRENCY)
                .amount(CANCELLED_OFFER_AMOUNT)
                .startDate(CANCELLED_OFFER_START_DATE)
                .endDate(CANCELLED_OFFER_END_DATE)
                .cancelledDate(CANCELLED_OFFER_CANCELLED_DATE)
                .build();
        OFFER_01 = OfferData
                .builder()
                .id(OFFER_ID_01)
                .name(OFFER_NAME_01)
                .description(OFFER_DESCRIPTION_01)
                .currency(OFFER_CURRENCY_01)
                .amount(OFFER_AMOUNT_01)
                .startDate(OFFER_START_DATE_01)
                .endDate(OFFER_END_DATE_01)
                .build();
    }

    @After
    public void tearDown() {
        offerRepository.deleteAll();
    }

    @Test
    public void should_exclude_cancelled_offers() {
        // GIVEN
        entityManager.persist(OFFER_01);
        entityManager.persist(CANCELLED_OFFER_01);

        // WHEN
        List<OfferData> offers = offerRepository.findByCancelledDateIsNull();

        // THEN
        assertThat(offers, hasSize(1));
        assertThat(offers,
                hasItem(both(hasProperty("id", equalTo(OFFER_ID_01)))
                        .and(hasProperty("name", equalTo(OFFER_NAME_01)))
                        .and(hasProperty("description", equalTo(OFFER_DESCRIPTION_01)))
                        .and(hasProperty("startDate", equalTo(OFFER_START_DATE_01)))
                        .and(hasProperty("endDate", equalTo(OFFER_END_DATE_01)))
                        .and(hasProperty("currency", equalTo(OFFER_CURRENCY_01)))
                        .and(hasProperty("amount", is(closeTo(OFFER_AMOUNT_01, BigDecimal.ZERO))))
                        .and(hasProperty("cancelledDate", is(nullValue())))

                )
        );
    }

    @Test
    public void should_not_find_cancelled_offer_by_id() {
        // GIVEN
        entityManager.persist(CANCELLED_OFFER_01);

        // WHEN
        Optional<OfferData> offer = offerRepository.findByIdAndCancelledDateIsNull(CANCELLED_OFFER_ID);

        // THEN
        assertThat(offer, is(Optional.empty()));
    }

    @Test
    public void can_still_find_cancelled_offer() {
        // GIVEN
        entityManager.persist(CANCELLED_OFFER_01);

        // WHEN
        Optional<OfferData> cancelledOffer = offerRepository.findById(CANCELLED_OFFER_ID);

        // THEN
        assertThat(cancelledOffer, is(not(Optional.empty())));
        assertThat(cancelledOffer.get(),
                both(hasProperty("id", equalTo(CANCELLED_OFFER_ID)))
                        .and(hasProperty("name", equalTo(CANCELLED_OFFER_NAME)))
                        .and(hasProperty("description", equalTo(CANCELLED_OFFER_DESCRIPTION)))
                        .and(hasProperty("startDate", equalTo(CANCELLED_OFFER_START_DATE)))
                        .and(hasProperty("endDate", equalTo(CANCELLED_OFFER_END_DATE)))
                        .and(hasProperty("currency", equalTo(CANCELLED_OFFER_CURRENCY)))
                        .and(hasProperty("amount", is(closeTo(CANCELLED_OFFER_AMOUNT, BigDecimal.ZERO))))
                        .and(hasProperty("cancelledDate", equalTo(CANCELLED_OFFER_CANCELLED_DATE)))
        );
    }

    @Test
    public void can_find_offer_by_name() {
        // GIVEN
        entityManager.persist(OFFER_01);

        // WHEN
        boolean exists = offerRepository.existsByNameAndCancelledDateIsNull(OFFER_NAME_01);

        // THEN
        assertThat(exists, is(true));
    }

    @Test
    public void cannot_find_cancelled_offer_by_name() {
        // GIVEN
        entityManager.persist(CANCELLED_OFFER_01);

        // WHEN
        boolean exists = offerRepository.existsByNameAndCancelledDateIsNull(CANCELLED_OFFER_NAME);

        // THEN
        assertThat(exists, is(false));
    }
}