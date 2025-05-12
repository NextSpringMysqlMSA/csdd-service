package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EuddAnswerRequest;
import com.nsmm.esg.csddservice.dto.EuddViolationDto;
import com.nsmm.esg.csddservice.entity.EuddAnswer;
import com.nsmm.esg.csddservice.repository.EuddAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EuddAnswerService {

    private final EuddAnswerRepository euddAnswerRepository;
    private final EuddViolationService euddViolationService;

    // 설문 저장 및 위반 항목 반환
    public List<String> saveAnswersAndGetViolatedQuestionIds(Long memberId, EuddAnswerRequest request) {
        List<EuddAnswer> answers = request.getAnswers().entrySet().stream()
                .map(entry -> EuddAnswer.builder()
                        .memberId(memberId)
                        .questionId(entry.getKey())
                        .answer(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        euddAnswerRepository.saveAll(answers);

        return answers.stream()
                .filter(a -> !a.isAnswer())
                .map(EuddAnswer::getQuestionId)
                .collect(Collectors.toList());
    }

    // 저장된 위반 항목 가져오기
    public List<EuddViolationDto> getStoredViolationsByMemberId(Long memberId) {
        List<String> violatedIds = euddAnswerRepository.findByMemberIdAndAnswerFalse(memberId)
                .stream()
                .map(EuddAnswer::getQuestionId)
                .collect(Collectors.toList());

        return euddViolationService.getViolationsByIds(violatedIds);
    }

    // 해당 멤버의 기존 답변 전체 삭제
    @Transactional
    public void deleteByMemberId(Long memberId) {
        euddAnswerRepository.deleteByMemberId(memberId);
    }

    // ✅ 설문 응답 수정 (기존 삭제 후 새로 저장)
    @Transactional
    public void updateAnswers(Long memberId, EuddAnswerRequest request) {
        // 기존 응답 삭제
        euddAnswerRepository.deleteByMemberId(memberId);

        // 새 응답 저장
        List<EuddAnswer> updatedAnswers = request.getAnswers().entrySet().stream()
                .map(entry -> EuddAnswer.builder()
                        .memberId(memberId)
                        .questionId(entry.getKey())
                        .answer(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        euddAnswerRepository.saveAll(updatedAnswers);
    }
}
