package com.grantburgess.database.inmemory;

import com.grantburgess.ports.database.Database;
import com.grantburgess.ports.database.OfferGateway;

public class InMemoryDatabase implements Database {
    private OfferGateway offerGateway = new InMemoryOfferGateway();

    public OfferGateway offerGateway() {
        return offerGateway;
    }
}
