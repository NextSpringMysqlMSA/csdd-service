package com.nsmm.esg.csddservice.repository;

import com.nsmm.esg.csddservice.entity.EuddAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EuddAnswerRepository extends JpaRepository<EuddAnswer, Long> {

    // 특정 사용자(memberId)의 모든 응답 조회
    List<EuddAnswer> findAllByMemberId(Long memberId);

    // "아니오(false)"로 응답한 항목만 조회
    List<EuddAnswer> findByMemberIdAndAnswerFalse(Long memberId);

    // 특정 사용자의 모든 응답 삭제
    void deleteByMemberId(Long memberId);
}