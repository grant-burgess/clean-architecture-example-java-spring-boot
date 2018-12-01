package com.grantburgess.database.jpa;

import com.grantburgess.database.jpa.data.OfferData;
import com.grantburgess.database.jpa.repositories.OfferRepository;
import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.OfferGateway;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaOfferGateway implements OfferGateway {
    private final OfferRepository offerRepository;

    public JpaOfferGateway(final OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Collection<Offer> getAllExcludingCancelled() {
        return offerRepository.findByCancelledDateIsNull()
                .stream()
                .map(this::mapToOffer)
                .collect(Collectors.toList());
    }

    private Offer mapToOffer(OfferData offerData) {
        return new Offer(
                offerData.getId(),
                offerData.getName(),
                offerData.getDescription(),
                offerData.getStartDate(),
                offerData.getEndDate(),
                new Offer.Money(offerData.getCurrency(), offerData.getAmount()),
                offerData.getCancelledDate()
        );
    }

    public UUID add(Offer offer) {
        UUID id = UUID.randomUUID();
        OfferData offerData = OfferData
                .builder()
                .id(id)
                .name(offer.getName())
                .description(offer.getDescription())
                .startDate(offer.getStartDate())
                .endDate(offer.getEndDate())
                .currency(offer.getPrice().getCurrency())
                .amount(offer.getPrice().getAmount())
                .cancelledDate(null)
                .build();
        offerRepository.save(offerData);

        return id;
    }

    public Offer getByIdExcludingCancelled(UUID id) {
        return offerRepository.findByIdAndCancelledDateIsNull(id)
                .map(this::mapToOffer)
                .orElse(null);
    }

    public void update(Offer offer) {
        OfferData offerData = OfferData
                .builder()
                .id(offer.getId())
                .name(offer.getName())
                .description(offer.getDescription())
                .currency(offer.getPrice().getCurrency())
                .amount(offer.getPrice().getAmount())
                .startDate(offer.getStartDate())
                .endDate(offer.getEndDate())
                .cancelledDate(offer.getCancelDate())
                .build();

        offerRepository.save(offerData);
    }

    public boolean existsByName(String name) {
        return offerRepository.existsByNameAndCancelledDateIsNull(name);
    }
}
