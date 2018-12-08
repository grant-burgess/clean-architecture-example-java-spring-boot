package com.grantburgess.ports.usescases.addoffer;

public interface AddOfferInputBoundary {
    NewOfferResponse execute(AddOfferRequest request);
}
