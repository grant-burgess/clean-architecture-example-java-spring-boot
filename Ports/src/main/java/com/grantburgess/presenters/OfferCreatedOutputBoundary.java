package com.grantburgess.presenters;

import com.grantburgess.usecases.addoffer.NewOfferResponse;

public interface OfferCreatedOutputBoundary {
    OfferCreatedViewModel getViewModel();
    void present(NewOfferResponse responseModel);
}
