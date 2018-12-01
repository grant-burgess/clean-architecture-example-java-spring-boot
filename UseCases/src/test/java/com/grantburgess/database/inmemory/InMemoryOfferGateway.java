package com.grantburgess.database.inmemory;

import com.grantburgess.entities.Offer;
import com.grantburgess.ports.database.OfferGateway;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryOfferGateway implements OfferGateway {
    private final Map<String, Offer> offers = new HashMap<>();

    public Collection<Offer> getAllExcludingCancelled() {
        return Collections.unmodifiableList(
                offers.values()
                        .stream()
                        .filter(InMemoryOfferGateway::excludeCancelledOffers)
                        .collect(Collectors.toList())
        );
    }

    private static boolean excludeCancelledOffers(Offer offer) {
        return offer.getCancelDate() == null;
    }

    public UUID add(Offer offer) {
        UUID uuid = UUID.randomUUID();
        offers.put(uuid.toString(), new Offer(uuid, offer.getName(), offer.getDescription(), offer.getStartDate(), offer.getEndDate(), offer.getPrice(), offer.getCancelDate()));

        return uuid;
    }

    public Offer getByIdExcludingCancelled(UUID id) {
        Offer offer = offers.get(id.toString());
        if (offer == null || offer.getCancelDate() != null)
            return null;

        return new Offer(offer.getId(), offer.getName(), offer.getDescription(), offer.getStartDate(), offer.getEndDate(), offer.getPrice(), offer.getCancelDate());
    }

    public void update(Offer offer) {
        offers.put(offer.getId().toString(), offer);
    }

    public boolean existsByName(String name) {
        return getAllExcludingCancelled()
                .stream()
                .anyMatch(offer -> offer.getName().equalsIgnoreCase(name));
    }
}
