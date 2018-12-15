package com.grantburgess.usecases.get.offers;

import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.ports.presenters.OffersOutputBoundary;
import com.grantburgess.ports.usescases.Clock;
import com.grantburgess.ports.usescases.get.offers.GetOfferInputBoundary;
import com.grantburgess.ports.usescases.get.offers.GetOffersRequest;
import com.grantburgess.ports.usescases.get.offers.OffersResponse;
import com.grantburgess.usecases.get.GetOfferBase;

public class GetOffers extends GetOfferBase implements GetOfferInputBoundary {
    private final OffersOutputBoundary presenter;
    private final OfferGateway offerGateway;
    private final Clock clock;

    public GetOffers(OffersOutputBoundary presenter, OfferGateway offerGateway, Clock clock) {
        this.presenter = presenter;
        this.offerGateway = offerGateway;
        this.clock = clock;
    }

    public void execute(GetOffersRequest request) {
        OffersResponse.OffersResponseBuilder result = OffersResponse.builder();
        offerGateway.getAllExcludingCancelled().forEach(offer -> result.addOffer(makeOfferResponse(offer, clock)));

        presenter.present(result.build());
    }
}
