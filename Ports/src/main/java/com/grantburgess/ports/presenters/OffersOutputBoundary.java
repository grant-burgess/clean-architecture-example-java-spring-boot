package com.grantburgess.ports.presenters;

import com.grantburgess.ports.usescases.get.offers.OffersResponse;

public interface OffersOutputBoundary {
    OffersViewModel getViewModel();
    void present(OffersResponse responseModel);
}
