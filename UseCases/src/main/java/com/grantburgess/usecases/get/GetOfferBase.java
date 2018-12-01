package com.grantburgess.usecases.get;

import com.grantburgess.entities.Offer;
import com.grantburgess.usecases.Clock;
import com.grantburgess.usecases.get.OfferResponse;

public abstract class GetOfferBase {
    public static OfferResponse makeOfferResponse(Offer offer, Clock clock) {
        OfferResponse.Status offerStatus = computeStatus(offer, clock);
        return new OfferResponse(
                offer.getId(),
                offer.getName(),
                offer.getDescription(),
                offer.getStartDate(),
                offer.getEndDate(),
                offer.getPrice().getCurrency(),
                offer.getPrice().getAmount(),
                offerStatus
        );
    }

    private static OfferResponse.Status computeStatus(Offer offer, Clock clock) {
        if (offer.getCancelDate() != null)
            return OfferResponse.Status.CANCELLED;

        return offer.getEndDate().isAfter(clock.now()) ?
                OfferResponse.Status.ACTIVE :
                OfferResponse.Status.EXPIRED;
    }
}
