package com.grantburgess.usecases.get.offerbyid;

import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.ports.presenters.OfferOutputBoundary;
import com.grantburgess.ports.usescases.Clock;
import com.grantburgess.usecases.get.GetOfferBase;
import com.grantburgess.ports.usescases.get.offerbyid.GetOfferByIdInputBoundary;
import com.grantburgess.ports.usescases.get.offerbyid.GetOfferRequest;

public class GetOfferById extends GetOfferBase implements GetOfferByIdInputBoundary {
    private final OfferOutputBoundary presenter;
    private final OfferGateway offerGateway;
    private final Clock clock;

    public GetOfferById(OfferOutputBoundary presenter, OfferGateway offerGateway, Clock clock) {
        this.presenter = presenter;
        this.offerGateway = offerGateway;
        this.clock = clock;
    }

    public void execute(GetOfferRequest request) {
        Offer offer = offerGateway.getByIdExcludingCancelled(request.getId());

        if (offer == null)
            throw new OfferGateway.OfferNotFoundException();

        presenter.present(makeOfferResponse(offer, clock));
    }
}
