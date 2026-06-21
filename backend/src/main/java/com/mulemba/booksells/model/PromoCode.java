package com.mulemba.booksells.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "codigos_promocionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Percentage (0.20) or flat amount in Kwanza */
    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private boolean percentage;

    @Column(nullable = false)
    private boolean active = true;
}
