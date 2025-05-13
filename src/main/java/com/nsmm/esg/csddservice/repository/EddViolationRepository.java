package com.nsmm.esg.csddservice.repository;

import com.nsmm.esg.csddservice.entity.EddViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EddViolationRepository extends JpaRepository<EddViolation, String> {
}
