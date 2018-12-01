package com.grantburgess.application.endpoints.addoffer;

import com.grantburgess.application.endpoints.BaseEndpoint;
import com.grantburgess.presenters.OfferCreatedOutputBoundary;
import com.grantburgess.usecases.addoffer.AddOfferInputBoundary;
import com.grantburgess.usecases.addoffer.AddOfferRequest;
import com.grantburgess.usecases.addoffer.NewOfferResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.text.MessageFormat;

@RestController
@RequestMapping("/api/v1/offers")
public class AddOfferEndpoint implements BaseEndpoint {
    private final AddOfferInputBoundary useCase;
    private final OfferCreatedOutputBoundary presenter;

    public AddOfferEndpoint(AddOfferInputBoundary useCase, OfferCreatedOutputBoundary presenter) {
        this.useCase = useCase;
        this.presenter = presenter;
    }

    @PostMapping
    public ResponseEntity execute(@RequestBody @Valid NewOfferRequest request) {
        NewOfferResponse responseModel = useCase.execute(
                AddOfferRequest
                        .builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .currency(request.getCurrency())
                        .price(request.getPrice())
                        .startDate(request.getDuration().getStartDate())
                        .endDate(request.getDuration().getEndDate())
                        .build()
        );
        presenter.present(responseModel);

        return ResponseEntity
                .created(
                        URI.create(
                                MessageFormat.format("/api/v1/offers/{0}", presenter.getViewModel().getId())
                        )
                )
                .body(presenter.getViewModel());
    }
}
