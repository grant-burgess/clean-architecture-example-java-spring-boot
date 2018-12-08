package com.grantburgess.usecases.get.offers;

import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.ports.usescases.Clock;
import com.grantburgess.usecases.get.GetOfferBase;
import com.grantburgess.ports.usescases.get.offers.GetOfferInputBoundary;
import com.grantburgess.ports.usescases.get.offers.GetOffersRequest;
import com.grantburgess.ports.usescases.get.offers.OffersResponse;

public class GetOffers extends GetOfferBase implements GetOfferInputBoundary {
    private final OfferGateway offerGateway;
    private final Clock clock;

    public GetOffers(OfferGateway offerGateway, Clock clock) {
        this.offerGateway = offerGateway;
        this.clock = clock;
    }

    public OffersResponse execute(GetOffersRequest request) {
        OffersResponse.OffersResponseBuilder result = OffersResponse.builder();

        offerGateway.getAllExcludingCancelled().forEach(offer -> result.addOffer(makeOfferResponse(offer, clock)));

        return result.build();
    }
}
