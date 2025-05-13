package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.HrddAnswerRequest;
import com.nsmm.esg.csddservice.dto.HrddViolationResponse;
import com.nsmm.esg.csddservice.entity.HrddAnswer;
import com.nsmm.esg.csddservice.repository.HrddAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrddAnswerService {

    private final HrddAnswerRepository hrddAnswerRepository;
    private final HrddViolationService hrddViolationService;

    /**
     * 설문 저장 및 위반 항목 반환
     */
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, HrddAnswerRequest request) {
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

}
