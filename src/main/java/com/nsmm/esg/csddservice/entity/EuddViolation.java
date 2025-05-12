package com.nsmm.esg.csddservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "eudd_violation")
public class EuddViolation {

    @Id
    private String id; // 예: "EUDD-1-01"

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private String legalRelevance;

    @Column(nullable = false)
    private String legalBasis;

    @Column(nullable = false)
    private String fineRange;

    @Column(nullable = false)
    private String criminalLiability;
}