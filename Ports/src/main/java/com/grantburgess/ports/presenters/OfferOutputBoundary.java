package com.grantburgess.ports.presenters;

import com.grantburgess.ports.usescases.get.OfferResponse;

public interface OfferOutputBoundary {
    OfferViewModel getViewModel();
    void present(OfferResponse responseModel);
}
