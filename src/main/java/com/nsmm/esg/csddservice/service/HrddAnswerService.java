package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.HrddAnswerRequest;
import com.nsmm.esg.csddservice.dto.HrddViolationResponse;
import com.nsmm.esg.csddservice.entity.HrddAnswer;
import com.nsmm.esg.csddservice.exception.InvalidAnswerException;
import com.nsmm.esg.csddservice.exception.UnauthorizedCsddAccessException;
import com.nsmm.esg.csddservice.repository.HrddAnswerRepository;
import com.nsmm.esg.csddservice.repository.HrddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HrddAnswerService {

    private final HrddAnswerRepository hrddAnswerRepository;
    private final HrddViolationService hrddViolationService;
    private final HrddViolationRepository hrddViolationRepository;

    /**
     * 설문 저장 및 위반 항목 반환
     */
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, HrddAnswerRequest request) {
        // 유효성 검사
        validateQuestionIds(request.getAnswers());

        // 저장
        List<HrddAnswer> answers = request.toEntities(memberId);
        hrddAnswerRepository.saveAll(answers);

        return answers.stream()
                .filter(a -> !a.isAnswer())
                .map(HrddAnswer::getQuestionId)
                .toList();
    }

    /**
     * 저장된 위반 항목 가져오기
     */
    public List<HrddViolationResponse> getStoredViolationsByMemberId(Long memberId) {
        List<String> violatedIds = hrddAnswerRepository.findByMemberIdAndAnswerFalse(memberId)
                .stream()
                .map(HrddAnswer::getQuestionId)
                .toList();

        return hrddViolationService.getViolationsByIds(violatedIds);
    }

    /**
     * 해당 멤버의 기존 응답 전체 삭제
     */
    @Transactional
    public void deleteByMemberId(Long memberId) {
        hrddAnswerRepository.deleteByMemberId(memberId);
    }

    /**
     * 존재하지 않는 질문 ID가 포함되었는지 검증
     */
    private void validateQuestionIds(Map<String, Boolean> answers) {
        List<String> validIds = hrddViolationRepository.findAll().stream()
                .map(v -> v.getId())
                .toList();

        for (String id : answers.keySet()) {
            if (!validIds.contains(id)) {
                throw new InvalidAnswerException("존재하지 않는 질문 ID입니다: " + id);
            } 
        }
    }


}