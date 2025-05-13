package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.HrddViolationResponse;
import com.nsmm.esg.csddservice.repository.HrddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HrddViolationService {

    private final HrddViolationRepository hrddViolationRepository;

    public List<HrddViolationResponse> getViolationsByIds(List<String> ids) {
        return hrddViolationRepository.findAllById(ids).stream()
                .map(HrddViolationResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
