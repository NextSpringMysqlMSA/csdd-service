package com.nsmm.esg.csddservice.service;

import com.nsmm.esg.csddservice.dto.EuddRequest;
import com.nsmm.esg.csddservice.entity.Eudd;
import com.nsmm.esg.csddservice.repository.EuddRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CsddService {

    private final EuddRepository euddRepository;

    /**
     * EUDD 데이터 저장
     */
    public String save(Long memberId, EuddRequest request) {
        Eudd entity = request.toEntity(memberId);
        return euddRepository.save(entity).getId();
    }

    /**
     * EUDD 목록 조회
     */
    public List<Eudd> getAll(Long memberId) {
        return euddRepository.findByMemberId(memberId);
    }

    /**
     * 특정 EUDD 조회
     */
    public Eudd getById(String id, Long memberId) {
        Eudd eudd = euddRepository.findById(id)
                .filter(e -> e.getMemberId().equals(memberId))
                .orElseThrow(() -> new IllegalArgumentException("조회할 데이터가 없거나 권한이 없습니다."));
        return eudd;
    }

    /**
     * EUDD 수정
     */
    public void update(String id, Long memberId, EuddRequest request) {
        Eudd eudd = euddRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 데이터가 존재하지 않습니다. ID = " + id));
        if (!eudd.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        eudd.updateFromDto(request);
    }

    /**
     * EUDD 삭제
     */
    public void delete(String id, Long memberId) {
        Eudd eudd = euddRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 데이터가 존재하지 않습니다. ID = " + id));
        if (!eudd.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        euddRepository.deleteById(id);
    }
}