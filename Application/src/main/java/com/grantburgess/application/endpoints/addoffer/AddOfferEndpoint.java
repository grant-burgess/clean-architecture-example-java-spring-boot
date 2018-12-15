package com.grantburgess.application.endpoints.addoffer;

import com.grantburgess.application.endpoints.BaseEndpoint;
import com.grantburgess.ports.presenters.OfferCreatedOutputBoundary;
import com.grantburgess.ports.presenters.OfferCreatedViewModel;
import com.grantburgess.ports.usescases.addoffer.AddOfferInputBoundary;
import com.grantburgess.ports.usescases.addoffer.AddOfferRequest;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "Add offer", response = OfferCreatedViewModel.class)
    public ResponseEntity execute(@RequestBody @Valid NewOfferRequest request) {
        useCase.execute(
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

        return ResponseEntity
                .created(
                        URI.create(
                                MessageFormat.format("/api/v1/offers/{0}", presenter.getViewModel().getId())
                        )
                )
                .body(presenter.getViewModel());
    }
}
