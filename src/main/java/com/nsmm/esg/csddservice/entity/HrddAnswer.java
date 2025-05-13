package com.nsmm.esg.csddservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 회원이 제출한 HRDD 자가진단 응답을 저장하는 엔티티
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hrdd_answer")
public class HrddAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키 (자동 증가)

    @Column(nullable = false)
    private Long memberId; // 응답을 제출한 회원 ID

    @Column(nullable = false, length = 50)
    private String questionId; // 질문 식별자 (예: "HRDD-1-01")

    @Column(nullable = false)
    private boolean answer; // 응답 값: true = "예", false = "아니요"

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 최초 응답 시간 (자동 설정)

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 응답 수정 시간 (자동 갱신)
}