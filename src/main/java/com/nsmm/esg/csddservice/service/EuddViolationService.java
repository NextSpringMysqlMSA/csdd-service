package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EuddViolationDto;
import com.nsmm.esg.csddservice.entity.EuddViolation;
import com.nsmm.esg.csddservice.repository.EuddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EuddViolationService {

    private final EuddViolationRepository euddViolationRepository;

    public List<EuddViolationDto> getViolationsByIds(List<String> ids) {
        return euddViolationRepository.findAllById(ids).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private EuddViolationDto toDto(EuddViolation entity) {
        return EuddViolationDto.builder()
                .id(entity.getId())
                .questionText(entity.getQuestionText())
                .legalRelevance(entity.getLegalRelevance())
                .legalBasis(entity.getLegalBasis())
                .fineRange(entity.getFineRange())
                .criminalLiability(entity.getCriminalLiability())
                .build();
    }
}
