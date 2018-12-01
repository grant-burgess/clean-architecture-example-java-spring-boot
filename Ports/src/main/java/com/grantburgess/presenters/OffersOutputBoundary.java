package com.grantburgess.presenters;

import com.grantburgess.usecases.get.offers.OffersResponse;

public interface OffersOutputBoundary {
    OffersViewModel getViewModel();
    void present(OffersResponse responseModel);
}
