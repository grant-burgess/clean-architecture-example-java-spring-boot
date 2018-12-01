package com.grantburgess.presenters;

import com.grantburgess.usecases.addoffer.NewOfferResponse;

public class OfferCreatedPresenter implements OfferCreatedOutputBoundary {
    private OfferCreatedViewModel viewModel;

    public OfferCreatedViewModel getViewModel() {
        return viewModel;
    }

    public void present(NewOfferResponse responseModel) {
        viewModel = new OfferCreatedViewModel(responseModel.getId().toString());
    }
}
