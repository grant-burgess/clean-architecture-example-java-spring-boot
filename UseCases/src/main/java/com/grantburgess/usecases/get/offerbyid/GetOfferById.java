package com.grantburgess.usecases.get.offerbyid;

import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.usecases.Clock;
import com.grantburgess.usecases.get.GetOfferBase;
import com.grantburgess.usecases.get.OfferResponse;

public class GetOfferById extends GetOfferBase implements GetOfferByIdInputBoundary {
    private final OfferGateway offerGateway;
    private final Clock clock;

    public GetOfferById(OfferGateway offerGateway, Clock clock) {
        this.offerGateway = offerGateway;
        this.clock = clock;
    }

    public OfferResponse execute(GetOfferRequest request) {
        Offer offer = offerGateway.getByIdExcludingCancelled(request.getId());

        if (offer == null)
            throw new OfferGateway.OfferNotFoundException();

        return makeOfferResponse(offer, clock);
    }
}
