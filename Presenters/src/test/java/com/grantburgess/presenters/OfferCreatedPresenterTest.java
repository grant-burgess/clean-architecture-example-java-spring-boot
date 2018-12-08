package com.grantburgess.presenters;

import com.grantburgess.ports.presenters.OfferCreatedViewModel;
import com.grantburgess.ports.usescases.addoffer.NewOfferResponse;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfferCreatedPresenterTest {
    @Test
    public void can_present_created_offer() {
        OfferCreatedPresenter presenter = new OfferCreatedPresenter();
        UUID id = UUID.randomUUID();
        // WHEN
        presenter.present(new NewOfferResponse(id));
        // THEN
        OfferCreatedViewModel viewModel = presenter.getViewModel();
        assertThat(viewModel, is(not(nullValue())));
        assertThat(id.toString(), equalTo(viewModel.getId()));
    }
}