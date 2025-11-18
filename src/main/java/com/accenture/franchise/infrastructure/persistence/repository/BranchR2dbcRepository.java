package com.accenture.franchise.infrastructure.persistence.repository;

import com.accenture.franchise.infrastructure.persistence.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface BranchR2dbcRepository extends R2dbcRepository<BranchEntity, Long> {
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);
}
