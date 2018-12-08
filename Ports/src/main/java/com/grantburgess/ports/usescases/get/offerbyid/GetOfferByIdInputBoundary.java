package com.grantburgess.ports.usescases.get.offerbyid;

import com.grantburgess.ports.usescases.get.OfferResponse;

public interface GetOfferByIdInputBoundary {
    OfferResponse execute(GetOfferRequest request);
}
