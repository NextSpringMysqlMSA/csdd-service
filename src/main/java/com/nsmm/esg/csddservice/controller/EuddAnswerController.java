package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.EuddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EuddViolationResponse;
import com.nsmm.esg.csddservice.service.EuddAnswerService;
import com.nsmm.esg.csddservice.service.EuddViolationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EU 공급망 실사 자가진단 응답 및 분석 결과 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/csdd/eudd")
@RequiredArgsConstructor
public class EuddAnswerController {

    private final EuddAnswerService euddAnswerService;
    private final EuddViolationService euddViolationService;

    /**
     * Gateway로부터 전달된 회원 ID 추출
     * - 누락된 경우 기본값 1L 사용 (테스트 목적)
     */
    private Long extractMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("X-MEMBER-ID");

        if (memberIdHeader == null || memberIdHeader.isBlank()) {
            System.out.println(" X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }

        return Long.parseLong(memberIdHeader);
    }

    /**
     * 저장된 자가진단 응답 기반 위반 항목 분석 결과 조회
     * - 주로 /CSDDD/eudd/result 페이지에서 호출
     */
    @GetMapping("/result")
    public List<EuddViolationResponse> getAnalysisResult(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return euddAnswerService.getStoredViolationsByMemberId(memberId);
    }

    /**
     * 기존 응답 갱신 (삭제 후 재저장) 및 위반 항목 분석
     * - 프론트에서 "저장" 버튼으로 호출됨
     */
    @PutMapping("/update")
    public List<EuddViolationResponse> updateAnswers(
            @RequestBody EuddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        euddAnswerService.deleteByMemberId(memberId); // 기존 응답 삭제
        List<String> violatedQuestionIds =
                euddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        return euddViolationService.getViolationsByIds(violatedQuestionIds);
    }
}
