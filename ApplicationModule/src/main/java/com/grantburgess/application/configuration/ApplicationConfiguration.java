package com.grantburgess.application.configuration;

import com.grantburgess.database.jpa.JpaDatabase;
import com.grantburgess.database.jpa.repositories.OfferRepository;
import com.grantburgess.ports.database.Database;
import com.grantburgess.ports.presenters.OfferCreatedOutputBoundary;
import com.grantburgess.ports.presenters.OfferOutputBoundary;
import com.grantburgess.ports.presenters.OffersOutputBoundary;
import com.grantburgess.ports.usescases.Clock;
import com.grantburgess.ports.usescases.addoffer.AddOfferInputBoundary;
import com.grantburgess.ports.usescases.canceloffer.CancelOfferInputBoundary;
import com.grantburgess.ports.usescases.get.offerbyid.GetOfferByIdInputBoundary;
import com.grantburgess.ports.usescases.get.offers.GetOfferInputBoundary;
import com.grantburgess.presenters.OfferCreatedPresenter;
import com.grantburgess.presenters.OfferPresenter;
import com.grantburgess.presenters.OffersPresenter;
import com.grantburgess.usecases.addoffer.AddOffer;
import com.grantburgess.usecases.canceloffer.CancelOffer;
import com.grantburgess.usecases.get.offerbyid.GetOfferById;
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
    public GetOfferByIdInputBoundary getOfferByIdInputBoundary(OfferOutputBoundary offerOutputBoundary, Database database, Clock clock) {
        return new GetOfferById(offerOutputBoundary, database.offerGateway(), clock);
    }

    @Bean
    public OfferOutputBoundary offerOutputBoundary() {
        return new OfferPresenter();
    }

    @Bean
    public AddOfferInputBoundary addOfferInputBoundary(OfferCreatedOutputBoundary offerCreatedOutputBoundary, Database database, Clock clock) {
        return new AddOffer(offerCreatedOutputBoundary, database.offerGateway(), clock);
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
