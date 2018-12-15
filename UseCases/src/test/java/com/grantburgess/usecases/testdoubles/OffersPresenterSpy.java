package com.grantburgess.usecases.testdoubles;

import com.grantburgess.ports.presenters.OffersOutputBoundary;
import com.grantburgess.ports.presenters.OffersViewModel;
import com.grantburgess.ports.usescases.get.offers.OffersResponse;

public class OffersPresenterSpy implements OffersOutputBoundary {
    private boolean isOffersPresented;
    private OffersResponse responseModel;

    public OffersViewModel getViewModel() {
        return null;
    }

    public void present(OffersResponse responseModel) {
        isOffersPresented = true;
        this.responseModel = responseModel;
    }

    public boolean isOffersPresented() {
        return isOffersPresented;
    }

    public OffersResponse getResponseModel() {
        return responseModel;
    }
}
