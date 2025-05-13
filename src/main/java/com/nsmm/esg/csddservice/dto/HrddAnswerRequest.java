package com.nsmm.esg.csddservice.dto;

import com.nsmm.esg.csddservice.entity.HrddAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 자가진단 응답 요청 DTO
 * - key: questionId (예: "HRDD-1-01")
 * - value: true(예), false(아니요)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrddAnswerRequest {
    private Map<String, Boolean> answers;


    /**
     * DTO → Entity 리스트 변환
     */
    public List<HrddAnswer> toEntities(Long memberId) {
        return answers.entrySet().stream()
                .map(entry -> HrddAnswer.builder()
                        .memberId(memberId)
                        .questionId(entry.getKey())
                        .answer(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
