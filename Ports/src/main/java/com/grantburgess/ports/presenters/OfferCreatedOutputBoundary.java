package com.grantburgess.ports.presenters;

import com.grantburgess.ports.usescases.addoffer.NewOfferResponse;

public interface OfferCreatedOutputBoundary {
    OfferCreatedViewModel getViewModel();
    void present(NewOfferResponse responseModel);
}
