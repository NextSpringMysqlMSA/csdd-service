package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.EuddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EuddViolationResponse;
import com.nsmm.esg.csddservice.service.EuddAnswerService;
import com.nsmm.esg.csddservice.service.EuddViolationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/csdd/eudd")
@RequiredArgsConstructor
public class EuddAnswerController {

    private final EuddAnswerService euddAnswerService;
    private final EuddViolationService euddViolationService;

    private Long extractMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("X-MEMBER-ID");
        if (memberIdHeader == null || memberIdHeader.isBlank()) {
            System.out.println("⚠️ X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }
        return Long.parseLong(memberIdHeader);
    }

    /**
     * 저장된 자가진단 응답 기반 위반 항목 분석 결과 조회
     */
    @GetMapping("/result")
    public ResponseEntity<List<EuddViolationResponse>> getAnalysisResult(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        List<EuddViolationResponse> result = euddAnswerService.getStoredViolationsByMemberId(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * 신규 설문 응답 저장 및 위반 항목 분석
     */
    @PostMapping
    public ResponseEntity<List<EuddViolationResponse>> submitAnswers(
            @RequestBody EuddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        List<String> violatedQuestionIds =
                euddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        List<EuddViolationResponse> result = euddViolationService.getViolationsByIds(violatedQuestionIds);
        return ResponseEntity.ok(result);
    }

    /**
     * 기존 응답 갱신 (삭제 후 재저장) 및 위반 항목 분석
     */
    @PutMapping("/update")
    public ResponseEntity<List<EuddViolationResponse>> updateAnswers(
            @RequestBody EuddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        // 🔐 기존 응답이 존재하는지 검증 (권한 확인)
        euddAnswerService.validateOwnership(memberId, request.getAnswers());

        euddAnswerService.deleteByMemberId(memberId); // 기존 응답 삭제
        List<String> violatedQuestionIds =
                euddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        List<EuddViolationResponse> result = euddViolationService.getViolationsByIds(violatedQuestionIds);
        return ResponseEntity.ok(result);
    }
}