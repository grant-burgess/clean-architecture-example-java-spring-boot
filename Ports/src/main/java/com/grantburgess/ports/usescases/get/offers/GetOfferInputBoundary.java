package com.grantburgess.ports.usescases.get.offers;

public interface GetOfferInputBoundary {
    OffersResponse execute(GetOffersRequest request);
}
