package com.nsmm.esg.csddservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EuddResultDto {
    private String id;
    private String questionText;
    private String legalRelevance;
    private String legalBasis;
    private String fineRange;
    private String criminalLiability;
}
