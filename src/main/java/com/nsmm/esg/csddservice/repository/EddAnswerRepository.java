package com.nsmm.esg.csddservice.repository;

import com.nsmm.esg.csddservice.entity.EddAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EddAnswerRepository extends JpaRepository<EddAnswer, Long> {


    // "아니오(false)"로 응답한 항목만 조회
    List<EddAnswer> findByMemberIdAndAnswerFalse(Long memberId);

    // 특정 사용자의 모든 응답 삭제
    void deleteByMemberId(Long memberId);
}