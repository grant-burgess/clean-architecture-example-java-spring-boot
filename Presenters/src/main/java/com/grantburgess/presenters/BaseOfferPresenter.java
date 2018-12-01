package com.grantburgess.presenters;

import com.grantburgess.usecases.get.OfferResponse;

import java.time.format.DateTimeFormatter;

public class BaseOfferPresenter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;

    public static OfferViewModel mapToOfferViewModel(OfferResponse responseModel) {
        return OfferViewModel
                .builder()
                .id(responseModel.getId().toString())
                .name(responseModel.getName())
                .description(responseModel.getDescription())
                .duration(getOfferDuration(responseModel))
                .currency(responseModel.getCurrency())
                .price(responseModel.getAmount().setScale(2).toPlainString())
                .status(responseModel.getStatus().name())
                .build();
    }

    private static OfferViewModel.Duration getOfferDuration(OfferResponse responseModel) {
        String startDate = responseModel.getStartDate().format(DATE_TIME_FORMATTER);
        String endDate = responseModel.getEndDate().format(DATE_TIME_FORMATTER);
        return OfferViewModel.Duration
                .builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
