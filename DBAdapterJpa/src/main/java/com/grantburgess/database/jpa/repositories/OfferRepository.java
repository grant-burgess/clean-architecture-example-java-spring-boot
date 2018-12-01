package com.grantburgess.database.jpa.repositories;

import com.grantburgess.database.jpa.data.OfferData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferRepository extends CrudRepository<OfferData, String> {
    List<OfferData> findByCancelledDateIsNull();
    Optional<OfferData> findByIdAndCancelledDateIsNull(UUID id);
    boolean existsByNameAndCancelledDateIsNull(String name);
}
