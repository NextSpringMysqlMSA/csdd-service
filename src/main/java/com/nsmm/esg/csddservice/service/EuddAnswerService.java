package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EuddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EuddViolationResponse;
import com.nsmm.esg.csddservice.entity.EuddAnswer;
import com.nsmm.esg.csddservice.repository.EuddAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EuddAnswerService {

    private final EuddAnswerRepository euddAnswerRepository;
    private final EuddViolationService euddViolationService;

    /**
     * 설문 저장 및 위반 항목 반환
     */
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, EuddAnswerRequest request) {
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
     * 응답 갱신 (삭제 후 재저장)
     */
    @Transactional
    public void updateAnswers(Long memberId, EuddAnswerRequest request) {
        euddAnswerRepository.deleteByMemberId(memberId);

        List<EuddAnswer> updatedAnswers = request.toEntities(memberId);
        euddAnswerRepository.saveAll(updatedAnswers);
    }
}
