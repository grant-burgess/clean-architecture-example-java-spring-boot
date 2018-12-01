package com.grantburgess.database.jpa.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Offer")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OfferData {
    @Id
    private UUID id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String currency;
    private BigDecimal amount;
    private LocalDate cancelledDate;
}
