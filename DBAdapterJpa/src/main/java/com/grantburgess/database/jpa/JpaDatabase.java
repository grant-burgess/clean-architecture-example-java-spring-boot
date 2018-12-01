package com.grantburgess.database.jpa;

import com.grantburgess.database.jpa.repositories.OfferRepository;
import com.grantburgess.ports.database.Database;
import com.grantburgess.ports.database.OfferGateway;

public class JpaDatabase implements Database {
    private OfferGateway offerGateway;

    public JpaDatabase(OfferRepository offerRepository) {
        offerGateway = new JpaOfferGateway(offerRepository);
    }


    public OfferGateway offerGateway() {
        return offerGateway;
    }
}
