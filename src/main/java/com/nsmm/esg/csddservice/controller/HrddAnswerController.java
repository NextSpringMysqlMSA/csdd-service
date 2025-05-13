package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.HrddAnswerRequest;
import com.nsmm.esg.csddservice.dto.HrddViolationResponse;
import com.nsmm.esg.csddservice.service.HrddAnswerService;
import com.nsmm.esg.csddservice.service.HrddViolationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 인권 실사 자가진단 응답 및 분석 결과 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/csdd/hrdd")
@RequiredArgsConstructor
public class HrddAnswerController {

    private final HrddAnswerService hrddAnswerService;
    private final HrddViolationService hrddViolationService;

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
     * - 주로 /CSDDD/hrdd/result 페이지에서 호출
     */
    @GetMapping("/result")
    public List<HrddViolationResponse> getAnalysisResult(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return hrddAnswerService.getStoredViolationsByMemberId(memberId);
    }

    /**
     * 최초 자가진단 응답 제출 및 위반 항목 분석 결과 반환
     * - 응답 저장 + 위반 questionId 식별 → violation 정보 반환
     */
    @PostMapping("/submit")
    public List<HrddViolationResponse> submitAnswers(
            @RequestBody HrddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        List<String> violatedQuestionIds =
                hrddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        return hrddViolationService.getViolationsByIds(violatedQuestionIds);
    }

    /**
     * 기존 응답 갱신 (삭제 후 재저장) 및 위반 항목 분석
     * - 프론트에서 "저장" 버튼으로 호출됨
     */
    @PutMapping("/update")
    public List<HrddViolationResponse> updateAnswers(
            @RequestBody HrddAnswerRequest request,
            HttpServletRequest httpRequest
    ) {
        Long memberId = extractMemberId(httpRequest);

        hrddAnswerService.deleteByMemberId(memberId); // 기존 응답 삭제
        List<String> violatedQuestionIds =
                hrddAnswerService.saveAnswersAndGetViolatedQuestionIds(memberId, request);

        return hrddViolationService.getViolationsByIds(violatedQuestionIds);
    }
}
