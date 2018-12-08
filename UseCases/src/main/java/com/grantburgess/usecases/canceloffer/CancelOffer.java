package com.grantburgess.usecases.canceloffer;

import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.OfferGateway;
import com.grantburgess.ports.usescases.Clock;
import com.grantburgess.ports.usescases.canceloffer.CancelOfferInputBoundary;
import com.grantburgess.ports.usescases.canceloffer.CancelOfferRequest;

public class CancelOffer implements CancelOfferInputBoundary {
    private final OfferGateway offerGateway;
    private final Clock clock;

    public CancelOffer(OfferGateway offerGateway, Clock clock) {
        this.offerGateway = offerGateway;
        this.clock = clock;
    }

    public void execute(CancelOfferRequest request) {
        Offer offer = getOffer(request);
        validateCancellationRequest(offer);
        offer.cancel(clock.now());
        offerGateway.update(offer);
    }

    private Offer getOffer(CancelOfferRequest request) {
        Offer offer = offerGateway.getByIdExcludingCancelled(request.getOfferId());
        if (offer == null)
            throw new OfferGateway.OfferNotFoundException();
        return offer;
    }

    private void validateCancellationRequest(Offer offer) {
        if (offer.getEndDate().isBefore(clock.now()))
            throw new OfferGateway.CannotCancelOfferThatHasExpiredException();
    }
}
