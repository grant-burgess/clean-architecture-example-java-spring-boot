package com.grantburgess.presenters;

import com.grantburgess.ports.presenters.OffersOutputBoundary;
import com.grantburgess.ports.presenters.OffersViewModel;
import com.grantburgess.ports.usescases.get.offers.OffersResponse;

public class OffersPresenter extends BaseOfferPresenter implements OffersOutputBoundary {
    private OffersViewModel viewModel;

    public OffersViewModel getViewModel() {
        return viewModel;
    }

    public void present(OffersResponse responseModel) {
        OffersViewModel.OffersViewModelBuilder offersViewModelBuilder = OffersViewModel.builder();
        responseModel.getOffers()
                .stream()
                .map(BaseOfferPresenter::mapToOfferViewModel)
                .forEach(offersViewModelBuilder::addOfferViewModel);
        viewModel = offersViewModelBuilder.build();
    }
}
