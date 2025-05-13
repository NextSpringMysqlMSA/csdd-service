package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EddViolationResponse;
import com.nsmm.esg.csddservice.entity.EddViolation;
import com.nsmm.esg.csddservice.repository.EddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EddViolationService {

    private final EddViolationRepository eddViolationRepository;

    public List<EddViolationResponse> getViolationsByIds(List<String> ids) {
        return eddViolationRepository.findAllById(ids).stream()
                .map(EddViolationResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
