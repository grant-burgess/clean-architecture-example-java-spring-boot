package com.grantburgess.usecases.get.offerbyid;

import com.grantburgess.usecases.get.OfferResponse;

public interface GetOfferByIdInputBoundary {
    OfferResponse execute(GetOfferRequest request);
}
