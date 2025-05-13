package com.nsmm.esg.csddservice.dto;

import com.nsmm.esg.csddservice.entity.HrddViolation;
import lombok.Builder;
import lombok.Getter;

/**
 * 위반 항목 분석 응답 DTO
 */
@Getter
@Builder
public class HrddViolationResponse {

    private final String id;
    private final String questionText;
    private final String legalRelevance;
    private final String legalBasis;
    private final String fineRange;
    private final String criminalLiability;

    /**
     * Entity → DTO 변환
     */
    public static HrddViolationResponse fromEntity(HrddViolation entity) {
        return HrddViolationResponse.builder()
                .id(entity.getId())
                .questionText(entity.getQuestionText())
                .legalRelevance(entity.getLegalRelevance())
                .legalBasis(entity.getLegalBasis())
                .fineRange(entity.getFineRange())
                .criminalLiability(entity.getCriminalLiability())
                .build();
    }
}
