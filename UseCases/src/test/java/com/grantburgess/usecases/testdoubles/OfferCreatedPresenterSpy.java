package com.grantburgess.usecases.testdoubles;

import com.grantburgess.ports.presenters.OfferCreatedOutputBoundary;
import com.grantburgess.ports.presenters.OfferCreatedViewModel;
import com.grantburgess.ports.usescases.addoffer.NewOfferResponse;

public class OfferCreatedPresenterSpy implements OfferCreatedOutputBoundary {
    private boolean isOfferPresented;
    private NewOfferResponse responseModel;

    public OfferCreatedViewModel getViewModel() {
        return null;
    }

    public void present(NewOfferResponse responseModel) {
        isOfferPresented = true;
        this.responseModel = responseModel;
    }

    public boolean isOfferPresented() {
        return isOfferPresented;
    }

    public NewOfferResponse getResponseModel() {
        return responseModel;
    }
}
