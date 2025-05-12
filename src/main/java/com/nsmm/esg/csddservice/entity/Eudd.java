package com.nsmm.esg.csddservice.entity;

import com.nsmm.esg.csddservice.dto.EuddRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "eudd")
public class Eudd {

    @Id
    private String id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String penalty;

    @Column(nullable = false)
    private String criminal;

    @Column(nullable = false)
    private String law;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateFromDto(EuddRequest request) {
        this.country = request.getCountry();
        this.type = request.getType();
        this.penalty = request.getPenalty();
        this.criminal = request.getCriminal();
        this.law = request.getLaw();
    }
}