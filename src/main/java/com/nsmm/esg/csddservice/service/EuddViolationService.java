package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EuddViolationResponse;
import com.nsmm.esg.csddservice.repository.EuddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EuddViolationService {

    private final EuddViolationRepository euddViolationRepository;

    public List<EuddViolationResponse> getViolationsByIds(List<String> ids) {
        return euddViolationRepository.findAllById(ids).stream()
                .map(EuddViolationResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
