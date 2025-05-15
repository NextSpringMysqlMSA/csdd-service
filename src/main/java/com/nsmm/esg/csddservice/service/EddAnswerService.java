package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EddViolationResponse;
import com.nsmm.esg.csddservice.entity.EddAnswer;
import com.nsmm.esg.csddservice.exception.InvalidAnswerException;
import com.nsmm.esg.csddservice.exception.UnauthorizedCsddAccessException;
import com.nsmm.esg.csddservice.repository.EddAnswerRepository;
import com.nsmm.esg.csddservice.repository.EddViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EddAnswerService {

    private final EddAnswerRepository eddAnswerRepository;
    private final EddViolationService eddViolationService;
    private final EddViolationRepository eddViolationRepository;

    /**
     * 설문 저장 및 위반 항목 반환
     */
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, EddAnswerRequest request) {
        // 유효성 검사
        validateQuestionIds(request.getAnswers());

        // 저장
        List<EddAnswer> answers = request.toEntities(memberId);
        eddAnswerRepository.saveAll(answers);

        return answers.stream()
                .filter(a -> !a.isAnswer())
                .map(EddAnswer::getQuestionId)
                .toList();
    }

    /**
     * 저장된 위반 항목 가져오기
     */
    public List<EddViolationResponse> getStoredViolationsByMemberId(Long memberId) {
        List<String> violatedIds = eddAnswerRepository.findByMemberIdAndAnswerFalse(memberId)
                .stream()
                .map(EddAnswer::getQuestionId)
                .toList();

        return eddViolationService.getViolationsByIds(violatedIds);
    }

    /**
     * 해당 멤버의 기존 응답 전체 삭제
     */
    @Transactional
    public void deleteByMemberId(Long memberId) {
        eddAnswerRepository.deleteByMemberId(memberId);
    }

    /**
     * 존재하지 않는 질문 ID가 포함되었는지 검증
     */
    private void validateQuestionIds(Map<String, Boolean> answers) {
        List<String> validIds = eddViolationRepository.findAll().stream()
                .map(v -> v.getId())
                .toList();

        for (String id : answers.keySet()) {
            if (!validIds.contains(id)) {
                throw new InvalidAnswerException("존재하지 않는 질문 ID입니다: " + id);
            }
        }
    }

    /**
     * 응답 주인이 맞는지 확인
     */
    public void validateOwnership(Long requestingMemberId, Map<String, Boolean> newAnswers) {
        List<String> existingQuestionIds = eddAnswerRepository.findByMemberIdAndAnswerFalse(requestingMemberId)
                .stream()
                .map(EddAnswer::getQuestionId)
                .toList();

        for (String id : newAnswers.keySet()) {
            if (!existingQuestionIds.contains(id)) {
                throw new UnauthorizedCsddAccessException("본인이 제출한 항목만 수정할 수 있습니다: " + id);
            }
        }
    }
}