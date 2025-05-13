package com.nsmm.esg.csddservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 위반 항목별 법적 책임 및 설명을 담는 기준 정보 엔티티
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "edd_violation")
public class EddViolation {

    @Id
    private String id; // 질문 ID (예: "EDD-1-01") — 기준 키

    @Column(nullable = false)
    private String questionText; // 질문 내용 (자가진단 질문 원문)

    @Column(nullable = false)
    private String legalRelevance; // 법적 해당 여부 설명

    @Column(nullable = false)
    private String legalBasis; // 관련 법령/조항 설명

    @Column(nullable = false)
    private String fineRange; // 벌금 범위

    @Column(nullable = false)
    private String criminalLiability; // 형사처벌 여부 설명
}
