package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.EddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EddViolationResponse;
import com.nsmm.esg.csddservice.service.EddAnswerService;
import com.nsmm.esg.csddservice.service.EddViolationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/csdd/edd")
@RequiredArgsConstructor
public class EddAnswerController {

    private final EddAnswerService eddAnswerService;
    private final EddViolationService eddViolationService;

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
    public ResponseEntity<List<EddViolationResponse>> getAnalysisResult(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        List<EddViolationResponse> result = eddAnswerService.getStoredViolationsByMemberId(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * 신규 설문 응답 저장 및 위반 항목 분석
     */
    @PostMapping
    public ResponseEntity<List<EddViolationResponse>> submitAnswers(
            @RequestBody EddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        List<String> violatedQuestionIds =
                eddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        List<EddViolationResponse> result = eddViolationService.getViolationsByIds(violatedQuestionIds);
        return ResponseEntity.ok(result);
    }

    /**
     * 기존 응답 갱신 (삭제 후 재저장) 및 위반 항목 분석
     */
    @PutMapping("/update")
    public ResponseEntity<List<EddViolationResponse>> updateAnswers(
            @RequestBody EddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        eddAnswerService.deleteByMemberId(memberId); // 기존 응답 삭제
        List<String> violatedQuestionIds =
                eddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        List<EddViolationResponse> result = eddViolationService.getViolationsByIds(violatedQuestionIds);
        return ResponseEntity.ok(result);
    }
}