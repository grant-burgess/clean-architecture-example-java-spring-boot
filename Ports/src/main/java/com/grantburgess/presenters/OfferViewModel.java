package com.grantburgess.presenters;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OfferViewModel {
    private String id;
    private String name;
    private String description;
    private Duration duration;
    private String currency;
    private String price;
    private String status;

    @Builder
    @Getter
    public static class Duration {
        private String startDate;
        private String endDate;
    }
}
