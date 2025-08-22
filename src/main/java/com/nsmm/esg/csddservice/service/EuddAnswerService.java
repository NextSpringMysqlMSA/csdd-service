package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EuddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EuddViolationResponse;
import com.nsmm.esg.csddservice.entity.EuddAnswer;
import com.nsmm.esg.csddservice.exception.InvalidAnswerException;
import com.nsmm.esg.csddservice.exception.UnauthorizedCsddAccessException;
import com.nsmm.esg.csddservice.repository.EuddAnswerRepository;
import com.nsmm.esg.csddservice.repository.EuddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EuddAnswerService {

    private final EuddAnswerRepository euddAnswerRepository;
    private final EuddViolationService euddViolationService;
    private final EuddViolationRepository euddViolationRepository;

    /**
     * 설문 저장 및 위반 항목 반환
     */
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, EuddAnswerRequest request) {
        // 유효성 검사
        validateQuestionIds(request.getAnswers());

        // 저장
        List<EuddAnswer> answers = request.toEntities(memberId);
        euddAnswerRepository.saveAll(answers);

        return answers.stream()
                .filter(a -> !a.isAnswer())
                .map(EuddAnswer::getQuestionId)
                .toList();
    }

    /**
     * 저장된 위반 항목 가져오기
     */
    public List<EuddViolationResponse> getStoredViolationsByMemberId(Long memberId) {
        List<String> violatedIds = euddAnswerRepository.findByMemberIdAndAnswerFalse(memberId)
                .stream()
                .map(EuddAnswer::getQuestionId)
                .toList();

        return euddViolationService.getViolationsByIds(violatedIds);
    }

    /**
     * 해당 멤버의 기존 응답 전체 삭제
     */
    @Transactional
    public void deleteByMemberId(Long memberId) {
        euddAnswerRepository.deleteByMemberId(memberId);
    }

    /**
     * 존재하지 않는 질문 ID가 포함되었는지 검증
     */
    private void validateQuestionIds(Map<String, Boolean> answers) {
        List<String> validIds = euddViolationRepository.findAll().stream()
                .map(v -> v.getId())
                .toList();

        for (String id : answers.keySet()) {
            if (!validIds.contains(id)) {
                throw new InvalidAnswerException("존재하지 않는 질문 ID입니다: " + id);
            }
        }
    }

}
