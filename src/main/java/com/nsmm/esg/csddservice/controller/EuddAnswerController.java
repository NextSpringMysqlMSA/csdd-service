package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.EuddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EuddViolationDto;
import com.nsmm.esg.csddservice.service.EuddAnswerService;
import com.nsmm.esg.csddservice.service.EuddViolationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/csdd/eudd")
@RequiredArgsConstructor
public class EuddAnswerController {

    private final EuddAnswerService euddAnswerService;
    private final EuddViolationService euddViolationService;

    /**
     * 공통적으로 사용하는 X-MEMBER-ID 추출 메서드
     */
    private Long extractMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("X-MEMBER-ID");

        if (memberIdHeader == null || memberIdHeader.isBlank()) {
            System.out.println("⚠️ X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }

        return Long.parseLong(memberIdHeader);
    }

    @GetMapping("/result")
    public List<EuddViolationDto> getAnalysisResult(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return euddAnswerService.getStoredViolationsByMemberId(memberId);
    }

    @PostMapping("/submit")
    public List<EuddViolationDto> submitAnswers(@RequestBody EuddAnswerRequest request, HttpServletRequest httpRequest) {
        Long memberId = extractMemberId(httpRequest);

        List<String> violatedQuestionIds = euddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);
        return euddViolationService.getViolationsByIds(violatedQuestionIds);
    }
    @PutMapping("/update")
    public List<EuddViolationDto> updateAnswers(@RequestBody EuddAnswerRequest request, HttpServletRequest httpRequest) {
        Long memberId = extractMemberId(httpRequest);

        // 기존 응답 제거 후 다시 저장
        euddAnswerService.deleteByMemberId(memberId);
        List<String> violatedQuestionIds = euddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        // 위반 항목 분석 후 결과 반환
        return euddViolationService.getViolationsByIds(violatedQuestionIds);
    }
}