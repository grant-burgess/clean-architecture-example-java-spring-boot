package com.grantburgess.application.endpoints.addoffer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "New Offer Request", subTypes = NewOfferRequest.Duration.class)
public class NewOfferRequest {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 25)
    @ApiModelProperty(notes = "Offer name", position = 1, example = "Offer name 01")
    private String name;
    @ApiModelProperty(notes = "Offer description", position = 2, example = "Offer description 01")
    private String description;
    @NotNull
    @Valid
    @ApiModelProperty(notes = "Offer duratoin", position = 5)
    private Duration duration;
    @NotBlank
    @NotNull
    @ApiModelProperty(notes = "Price currency", position = 3, example = "GBP")
    private String currency;
    @Positive
    @ApiModelProperty(notes = "Price amount", position = 4, example = "10.00")
    private BigDecimal price;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "Offer duration", parent = NewOfferRequest.class)
    public static class Duration {
        @NotNull
        @ApiModelProperty(notes = "Offer start date", position = 1, example = "2020-01-01")
        private LocalDate startDate;
        @NotNull
        @ApiModelProperty(notes = "Offer end date", position = 2, example = "2020-01-31")
        private LocalDate endDate;
    }
}
