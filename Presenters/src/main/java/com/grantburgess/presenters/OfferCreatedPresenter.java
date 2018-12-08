package com.grantburgess.presenters;

import com.grantburgess.ports.presenters.OfferCreatedOutputBoundary;
import com.grantburgess.ports.presenters.OfferCreatedViewModel;
import com.grantburgess.ports.usescases.addoffer.NewOfferResponse;

public class OfferCreatedPresenter implements OfferCreatedOutputBoundary {
    private OfferCreatedViewModel viewModel;

    public OfferCreatedViewModel getViewModel() {
        return viewModel;
    }

    public void present(NewOfferResponse responseModel) {
        viewModel = new OfferCreatedViewModel(responseModel.getId().toString());
    }
}
