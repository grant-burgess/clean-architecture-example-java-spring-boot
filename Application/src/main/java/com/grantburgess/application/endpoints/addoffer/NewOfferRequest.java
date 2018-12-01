package com.grantburgess.application.endpoints.addoffer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NewOfferRequest {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 25)
    private String name;
    private String description;
    @NotNull
    @Valid
    private Duration duration;
    @NotBlank
    @NotNull
    private String currency;
    @Positive
    private BigDecimal price;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Duration {
        @NotNull
        private LocalDate startDate;
        @NotNull
        private LocalDate endDate;
    }
}
