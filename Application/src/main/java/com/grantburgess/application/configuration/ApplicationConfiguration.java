package com.grantburgess.application.configuration;

import com.grantburgess.database.jpa.JpaDatabase;
import com.grantburgess.database.jpa.repositories.OfferRepository;
import com.grantburgess.ports.database.Database;
import com.grantburgess.presenters.*;
import com.grantburgess.usecases.Clock;
import com.grantburgess.usecases.addoffer.AddOffer;
import com.grantburgess.usecases.addoffer.AddOfferInputBoundary;
import com.grantburgess.usecases.canceloffer.CancelOffer;
import com.grantburgess.usecases.canceloffer.CancelOfferInputBoundary;
import com.grantburgess.usecases.get.offerbyid.GetOfferById;
import com.grantburgess.usecases.get.offerbyid.GetOfferByIdInputBoundary;
import com.grantburgess.usecases.get.offers.GetOfferInputBoundary;
import com.grantburgess.usecases.get.offers.GetOffers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public Database database(OfferRepository offerRepository) {
        return new JpaDatabase(offerRepository);
    }

    @Bean
    public Clock clock() {
        return new Clock() {
            @Override
            public LocalDate now() {
                return LocalDate.now();
            }
        };
    }

    @Bean
    public GetOfferInputBoundary getOfferInputBoundary(Database database, Clock clock) {
        return new GetOffers(database.offerGateway(), clock);
    }

    @Bean
    public OffersOutputBoundary offersOutputBoundary() {
        return new OffersPresenter();
    }

    @Bean
    public GetOfferByIdInputBoundary getOfferByIdInputBoundary(Database database, Clock clock) {
        return new GetOfferById(database.offerGateway(), clock);
    }

    @Bean
    public OfferOutputBoundary offerOutputBoundary() {
        return new OfferPresenter();
    }

    @Bean
    public AddOfferInputBoundary addOfferInputBoundary(Database database, Clock clock) {
        return new AddOffer(database.offerGateway(), clock);
    }

    @Bean
    public OfferCreatedOutputBoundary offerCreatedOutputBoundary() {
        return new OfferCreatedPresenter();
    }

    @Bean
    public CancelOfferInputBoundary cancelOfferInputBoundary(Database database, Clock clock) {
        return new CancelOffer(database.offerGateway(), clock);
    }
}
