package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.HrddAnswerRequest;
import com.nsmm.esg.csddservice.dto.HrddViolationResponse;
import com.nsmm.esg.csddservice.service.HrddAnswerService;
import com.nsmm.esg.csddservice.service.HrddViolationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/csdd/hrdd")
@RequiredArgsConstructor
public class HrddAnswerController {

    private final HrddAnswerService hrddAnswerService;
    private final HrddViolationService hrddViolationService;

    private Long extractMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("X-MEMBER-ID");
        if (memberIdHeader == null || memberIdHeader.isBlank()) {
            System.out.println("X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }
        return Long.parseLong(memberIdHeader);
    }

    /**
     * 저장된 자가진단 응답 기반 위반 항목 분석 결과 조회
     */
    @GetMapping("/result")
    public ResponseEntity<List<HrddViolationResponse>> getAnalysisResult(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        List<HrddViolationResponse> result = hrddAnswerService.getStoredViolationsByMemberId(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * 신규 설문 응답 저장 및 위반 항목 분석
     */
    @PostMapping
    public ResponseEntity<List<HrddViolationResponse>> submitAnswers(
            @RequestBody HrddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        List<String> violatedQuestionIds =
                hrddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        List<HrddViolationResponse> result = hrddViolationService.getViolationsByIds(violatedQuestionIds);
        return ResponseEntity.ok(result);
    }

    /**
     * 기존 응답 갱신 (삭제 후 재저장) 및 위반 항목 분석
     */
    @PutMapping("/update")
    public ResponseEntity<List<HrddViolationResponse>> updateAnswers(
            @RequestBody HrddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        hrddAnswerService.deleteByMemberId(memberId); // 기존 응답 삭제
        List<String> violatedQuestionIds =
                hrddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        List<HrddViolationResponse> result = hrddViolationService.getViolationsByIds(violatedQuestionIds);
        return ResponseEntity.ok(result);
    }
}