package com.nsmm.esg.csddservice.repository;

import com.nsmm.esg.csddservice.entity.Eudd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EuddRepository extends JpaRepository<Eudd, String> {
    List<Eudd> findByMemberId(Long memberId);
}