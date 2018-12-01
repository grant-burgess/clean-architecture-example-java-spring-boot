package com.grantburgess.application.exception;

import com.grantburgess.ports.database.OfferGateway;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessageMap {
    public static Map<Class, String> errors = new HashMap<>();

    static {
        errors.put(OfferGateway.OfferNotFoundException.class, "Offer not found");
        errors.put(OfferGateway.CannotCancelOfferThatHasExpiredException.class, "Cannot cancel expired offer");
        errors.put(OfferGateway.OfferEndDateCannotBeBeforeCurrentSystemDateException.class, "Offer end date must be earlier than the current date");
        errors.put(OfferGateway.OfferNameAlreadyExistsException.class, "Offer name already exists");
        errors.put(OfferGateway.OfferStartDateGreaterThanEndDateException.class, "Offer start date must be less than end date");
    }
}
