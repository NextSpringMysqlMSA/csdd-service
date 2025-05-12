package com.nsmm.esg.csddservice.repository;

import com.nsmm.esg.csddservice.entity.EuddViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EuddViolationRepository extends JpaRepository<EuddViolation, String> {
}
