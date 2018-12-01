package com.grantburgess.application.endpoints.canceloffer;

import com.grantburgess.application.endpoints.BaseEndpoint;
import com.grantburgess.usecases.canceloffer.CancelOfferInputBoundary;
import com.grantburgess.usecases.canceloffer.CancelOfferRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
public class CancelOfferEndpoint implements BaseEndpoint {
    private CancelOfferInputBoundary useCase;

    public CancelOfferEndpoint(CancelOfferInputBoundary useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/{offerId}/cancel")
    @ApiOperation(value = "Cancel offer", response = ResponseEntity.class)
    public ResponseEntity execute(@PathVariable(value = "offerId") String offerId) {
        useCase.execute(CancelOfferRequest.builder().offerId(UUID.fromString(offerId)).build());

        return ResponseEntity.noContent().build();
    }
}
