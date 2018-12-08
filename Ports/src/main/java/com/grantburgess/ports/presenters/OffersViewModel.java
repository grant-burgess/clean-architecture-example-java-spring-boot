package com.grantburgess.ports.presenters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OffersViewModel {
    @Singular("addOfferViewModel") private List<OfferViewModel> offers;
}
