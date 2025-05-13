package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EddViolationResponse;
import com.nsmm.esg.csddservice.entity.EddAnswer;
import com.nsmm.esg.csddservice.repository.EddAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EddAnswerService {

    private final EddAnswerRepository eddAnswerRepository;
    private final EddViolationService eddViolationService;

    /**
     * 설문 저장 및 위반 항목 반환
     */
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, EddAnswerRequest request) {
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
     * 응답 갱신 (삭제 후 재저장)
     */
    @Transactional
    public void updateAnswers(Long memberId, EddAnswerRequest request) {
        eddAnswerRepository.deleteByMemberId(memberId);

        List<EddAnswer> updatedAnswers = request.toEntities(memberId);
        eddAnswerRepository.saveAll(updatedAnswers);
    }
}
