package com.grantburgess.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grantburgess.application.endpoints.addoffer.AddOfferEndpoint;
import com.grantburgess.application.endpoints.canceloffer.CancelOfferEndpoint;
import com.grantburgess.application.endpoints.getofferbyid.GetOfferByIdEndpoint;
import com.grantburgess.application.endpoints.getoffers.GetOffersEndpoint;
import com.grantburgess.database.jpa.repositories.OfferRepository;
import com.grantburgess.ports.presenters.*;
import com.grantburgess.ports.usescases.Clock;
import com.grantburgess.ports.usescases.addoffer.AddOfferInputBoundary;
import com.grantburgess.ports.usescases.canceloffer.CancelOfferInputBoundary;
import com.grantburgess.ports.usescases.get.OfferResponse;
import com.grantburgess.ports.usescases.get.offerbyid.GetOfferByIdInputBoundary;
import com.grantburgess.ports.usescases.get.offers.GetOfferInputBoundary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {AddOfferEndpoint.class, CancelOfferEndpoint.class, GetOfferByIdEndpoint.class, GetOffersEndpoint.class})
@AutoConfigureJsonTesters
@WebAppConfiguration
public class EndpointTests {
    private final String NAME = "name-1";
    private final String DESCRIPTION = "description-1";
    private final BigDecimal AMOUNT = BigDecimal.TEN;
    private final String PRICE = AMOUNT.toPlainString();
    private final String CURRENCY = "GBP";
    private final String START_DATE = "2018-01-01";
    private final String END_DATE = "2018-01-31";
    private final String STATUS = "ACTIVE";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AddOfferInputBoundary addOfferInputBoundary;
    @MockBean
    private OfferCreatedOutputBoundary offerCreatedOutputBoundary;
    @MockBean
    private OfferRepository offerRepository;
    @MockBean
    private GetOfferByIdInputBoundary getOfferByIdInputBoundary;
    @MockBean
    private OfferOutputBoundary offerOutputBoundary;
    @MockBean
    private GetOfferInputBoundary getOfferInputBoundary;
    @MockBean
    private OffersOutputBoundary offersOutputBoundary;
    @MockBean
    private CancelOfferInputBoundary cancelOfferInputBoundary;
    @MockBean
    private Clock clock;

    private OfferViewModel buildOfferViewModel(String id, String name, String description, String price, String currency, String startDate, String endDate, String status) {
        return OfferViewModel
                .builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .currency(currency)
                .duration(
                        OfferViewModel.Duration
                                .builder()
                                .startDate(startDate)
                                .endDate(endDate)
                                .build()
                )
                .status(status)
                .build();
    }

    private byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    @Before
    public void setUp() {
        when(clock.now()).thenReturn(LocalDate.of(2018, 01, 15));
    }

    @Test
    public void can_create_offer() throws Exception {
        NewOfferRequest request = new NewOfferRequest(
                NAME,
                DESCRIPTION,
                START_DATE,
                END_DATE,
                CURRENCY,
                AMOUNT
        );
        String id = UUID.randomUUID().toString();
        when(offerCreatedOutputBoundary.getViewModel()).thenReturn(new OfferCreatedViewModel(id));
        mockMvc.perform(
                post(URI.create("/api/v1/offers"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(convertObjectToJsonBytes(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(id))));
    }

    @Test
    public void can_get_offer_by_id() throws Exception {
        String id = UUID.randomUUID().toString();
        OfferViewModel offerViewModel = buildOfferViewModel(id, NAME, DESCRIPTION, PRICE, CURRENCY, START_DATE, END_DATE, STATUS);
        when(getOfferByIdInputBoundary.execute(any())).thenReturn(new OfferResponse());
        when(offerOutputBoundary.getViewModel()).thenReturn(offerViewModel);
        mockMvc.perform(
                get("/api/v1/offers/{offerId}", id)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(id))))
                .andExpect(jsonPath("$.name", is(equalTo(NAME))))
                .andExpect(jsonPath("$.description", is(equalTo(DESCRIPTION))))
                .andExpect(jsonPath("$.price", is(equalTo(PRICE))))
                .andExpect(jsonPath("$.currency", is(equalTo(CURRENCY))))
                .andExpect(jsonPath("$.duration.startDate", is(equalTo(START_DATE))))
                .andExpect(jsonPath("$.duration.endDate", is(equalTo(END_DATE))))
                .andExpect(jsonPath("$.status", is(equalTo(STATUS))));
    }

    @Test
    public void can_get_offers() throws Exception {
        String id = UUID.randomUUID().toString();
        OfferViewModel offerViewModel = buildOfferViewModel(id, NAME, DESCRIPTION, PRICE, CURRENCY, START_DATE, END_DATE, STATUS);
        String id2 = UUID.randomUUID().toString();

        OfferViewModel offerViewModel2 = buildOfferViewModel(id2, "name-2", "", BigDecimal.ONE.setScale(2).toPlainString(), CURRENCY, "2018-01-05", "2018-01-31", "EXPIRED");
        when(offersOutputBoundary.getViewModel()).thenReturn(OffersViewModel.builder().addOfferViewModel(offerViewModel).addOfferViewModel(offerViewModel2).build());
        mockMvc.perform(
                get("/api/v1/offers")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offers", hasSize(2)))

                .andExpect(jsonPath("$.offers.[0].id", is(equalTo(id))))
                .andExpect(jsonPath("$.offers.[0].name", is(equalTo(NAME))))
                .andExpect(jsonPath("$.offers.[0].description", is(equalTo(DESCRIPTION))))
                .andExpect(jsonPath("$.offers.[0].price", is(equalTo(PRICE))))
                .andExpect(jsonPath("$.offers.[0].currency", is(equalTo(CURRENCY))))
                .andExpect(jsonPath("$.offers.[0].duration.startDate", is(equalTo(START_DATE))))
                .andExpect(jsonPath("$.offers.[0].duration.endDate", is(equalTo(END_DATE))))
                .andExpect(jsonPath("$.offers.[0].status", is(equalTo(STATUS))))

                .andExpect(jsonPath("$.offers.[1].id", is(equalTo(id2))))
                .andExpect(jsonPath("$.offers.[1].name", is(equalTo("name-2"))))
                .andExpect(jsonPath("$.offers.[1].description", is(isEmptyString())))
                .andExpect(jsonPath("$.offers.[1].price", is(equalTo("1.00"))))
                .andExpect(jsonPath("$.offers.[1].currency", is(equalTo("GBP"))))
                .andExpect(jsonPath("$.offers.[1].duration.startDate", is(equalTo("2018-01-05"))))
                .andExpect(jsonPath("$.offers.[1].duration.endDate", is(equalTo("2018-01-31"))))
                .andExpect(jsonPath("$.offers.[1].status", is(equalTo("EXPIRED"))));
    }

    @Test
    public void can_cancel_offer() throws Exception {
        String offerId = UUID.randomUUID().toString();
        mockMvc.perform(
                post("/api/v1/offers/{offerId}/cancel", offerId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(status().isNoContent());
    }

    private class NewOfferRequest {
        private String name;
        private String description;
        private Duration duration;
        private String currency;
        private BigDecimal price;

        public NewOfferRequest(String name, String description, String startDate, String endDate, String currency, BigDecimal price) {
            this.name = name;
            this.description = description;
            this.duration = new Duration(startDate, endDate);
            this.currency = currency;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Duration getDuration() {
            return duration;
        }

        public String getCurrency() {
            return currency;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public class Duration {
            private String startDate;
            private String endDate;

            public Duration(String startDate, String endDate) {
                this.startDate = startDate;
                this.endDate = endDate;
            }

            public String getStartDate() {
                return startDate;
            }

            public String getEndDate() {
                return endDate;
            }
        }
    }

}
