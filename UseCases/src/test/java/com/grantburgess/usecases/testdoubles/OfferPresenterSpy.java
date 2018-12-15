package com.grantburgess.usecases.testdoubles;

import com.grantburgess.ports.presenters.OfferOutputBoundary;
import com.grantburgess.ports.presenters.OfferViewModel;
import com.grantburgess.ports.usescases.get.OfferResponse;

public class OfferPresenterSpy implements OfferOutputBoundary {
    private boolean isOfferPresented;
    private OfferResponse responseModel;

    public OfferViewModel getViewModel() {
        return null;
    }

    public void present(OfferResponse responseModel) {
        isOfferPresented = true;
        this.responseModel = responseModel;
    }

    public boolean isOfferPresented() {
        return isOfferPresented;
    }

    public OfferResponse getResponseModel() {
        return responseModel;
    }
}
