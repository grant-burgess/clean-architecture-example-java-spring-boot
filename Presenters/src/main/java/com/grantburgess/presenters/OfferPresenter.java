package com.grantburgess.presenters;

import com.grantburgess.ports.presenters.OfferOutputBoundary;
import com.grantburgess.ports.presenters.OfferViewModel;
import com.grantburgess.ports.usescases.get.OfferResponse;

public class OfferPresenter extends BaseOfferPresenter implements OfferOutputBoundary {
    private OfferViewModel viewModel;

    public OfferViewModel getViewModel() {
        return viewModel;
    }

    public void present(OfferResponse responseModel) {
        viewModel = mapToOfferViewModel(responseModel);
    }
}
