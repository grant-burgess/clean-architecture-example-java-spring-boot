package com.grantburgess.presenters;

import com.grantburgess.usecases.get.OfferResponse;

public interface OfferOutputBoundary {
    OfferViewModel getViewModel();
    void present(OfferResponse responseModel);
}
