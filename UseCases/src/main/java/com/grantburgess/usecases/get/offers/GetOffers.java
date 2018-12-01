package com.grantburgess.usecases.get.offers;

import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.usecases.Clock;
import com.grantburgess.usecases.get.GetOfferBase;

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
