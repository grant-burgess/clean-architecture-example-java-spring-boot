package com.grantburgess.ports.usescases.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponse {
    private UUID id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String currency;
    private BigDecimal amount;
    private Status status;

    public enum Status {
        ACTIVE, EXPIRED, CANCELLED
    }
}
