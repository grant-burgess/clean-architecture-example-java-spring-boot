package com.grantburgess.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Offer {
    private final UUID id;
    private final String name;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Money price;
    private LocalDate cancelDate;

    public enum Status {
        ACTIVE, EXPIRED, CANCELLED
    }

    public Offer(String name, String description, LocalDate startDate, LocalDate endDate, Money price) {
        this(UUID.randomUUID(), name, description, startDate, endDate, price, null);
    }

    public Offer(UUID id, String name, String description, LocalDate startDate, LocalDate endDate, Money price, LocalDate cancelDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.cancelDate = cancelDate;
    }

    public void cancel(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Getter
    public static class Money {
        private final String currency;
        private final BigDecimal amount;

        public Money(String currency, BigDecimal amount) {
            this.currency = currency;
            this.amount = amount;
        }
    }
}
