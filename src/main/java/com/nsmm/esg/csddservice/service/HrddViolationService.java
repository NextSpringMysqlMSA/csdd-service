package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.HrddViolationResponse;
import com.nsmm.esg.csddservice.entity.HrddViolation;
import com.nsmm.esg.csddservice.exception.ViolationNotFoundException;
import com.nsmm.esg.csddservice.repository.HrddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrddViolationService {

    private final HrddViolationRepository hrddViolationRepository;

    public List<HrddViolationResponse> getViolationsByIds(List<String> ids) {
        // DB에서 실제 존재하는 항목 조회
        List<HrddViolation> foundEntities = hrddViolationRepository.findAllById(ids);

        // 조회된 ID 목록
        List<String> foundIds = foundEntities.stream()
                .map(HrddViolation::getId)
                .toList();

        // 요청한 ID 중 누락된 것 찾기
        List<String> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ViolationNotFoundException("존재하지 않는 HRDD 위반 항목 ID: " + String.join(", ", missingIds));
        }

        // 응답 DTO로 변환
        return foundEntities.stream()
                .map(HrddViolationResponse::fromEntity)
                .toList();
    }
}