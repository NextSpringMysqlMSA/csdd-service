package com.nsmm.esg.csddservice.dto;

import com.nsmm.esg.csddservice.entity.Eudd;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EuddRequest {

    private final String id;
    private final String country;
    private final String type;
    private final String penalty;
    private final String criminal;
    private final String law;

    // DTO → Entity
    public Eudd toEntity(Long memberId) {
        return Eudd.builder()
                .id(id)
                .memberId(memberId)
                .country(country)
                .type(type)
                .penalty(penalty)
                .criminal(criminal)
                .law(law)
                .build();
    }

    // Entity → DTO
    public static EuddRequest fromEntity(Eudd entity) {
        return EuddRequest.builder()
                .id(entity.getId())
                .country(entity.getCountry())
                .type(entity.getType())
                .penalty(entity.getPenalty())
                .criminal(entity.getCriminal())
                .law(entity.getLaw())
                .build();
    }
}
