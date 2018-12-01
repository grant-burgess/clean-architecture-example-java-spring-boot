package com.grantburgess.application.endpoints.getoffers;

import com.grantburgess.application.endpoints.BaseEndpoint;
import com.grantburgess.presenters.OffersOutputBoundary;
import com.grantburgess.presenters.OffersViewModel;
import com.grantburgess.usecases.get.offers.GetOfferInputBoundary;
import com.grantburgess.usecases.get.offers.GetOffersRequest;
import com.grantburgess.usecases.get.offers.OffersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/offers")
public class GetOffersEndpoint implements BaseEndpoint {
    private final GetOfferInputBoundary useCase;
    private final OffersOutputBoundary presenter;

    public GetOffersEndpoint(GetOfferInputBoundary useCase, OffersOutputBoundary presenter) {
        this.useCase = useCase;
        this.presenter = presenter;
    }

    @GetMapping
    public ResponseEntity execute() {
        OffersResponse responseModel = useCase.execute(new GetOffersRequest());
        presenter.present(responseModel);

        return ResponseEntity.ok(presenter.getViewModel());
    }
}
