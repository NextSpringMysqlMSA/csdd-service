package com.nsmm.esg.csddservice.controller;

import com.nsmm.esg.csddservice.dto.EuddRequest;
import com.nsmm.esg.csddservice.entity.Eudd;
import com.nsmm.esg.csddservice.service.CsddService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/csdd/eudd")
public class CsddController {

    private final CsddService csddService;

    private Long extractMemberId(HttpServletRequest request) {
        String memberIdHeader = request.getHeader("X-MEMBER-ID");

        if (memberIdHeader == null || memberIdHeader.isBlank()) {
            System.out.println("⚠️ X-MEMBER-ID 누락 → 기본값 1L 사용");
            return 1L;
        }

        return Long.parseLong(memberIdHeader);
    }

    /**
     * EUDD 전체 목록 조회
     */
    @GetMapping("/eudd")
    public List<Eudd> getAll(HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return csddService.getAll(memberId);
    }

    /**
     * 특정 EUDD 조회
     */
    @GetMapping("/eudd/{id}")
    public Eudd getById(@PathVariable String id, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        return csddService.getById(id, memberId);
    }

    /**
     * EUDD 저장
     */
    @PostMapping("/eudd")
    public String create(@RequestBody EuddRequest requestDto, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        String id = csddService.save(memberId, requestDto);
        return "EUDD 저장 완료. ID = " + id;
    }

    /**
     * EUDD 수정
     */
    @PutMapping("/eudd/{id}")
    public String update(@PathVariable String id, @RequestBody EuddRequest requestDto, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        csddService.update(id, memberId, requestDto);
        return "EUDD 수정 완료. ID = " + id;
    }

    /**
     * EUDD 삭제
     */
    @DeleteMapping("/eudd/{id}")
    public String delete(@PathVariable String id, HttpServletRequest request) {
        Long memberId = extractMemberId(request);
        csddService.delete(id, memberId);
        return "EUDD 삭제 완료. ID = " + id;
    }
}