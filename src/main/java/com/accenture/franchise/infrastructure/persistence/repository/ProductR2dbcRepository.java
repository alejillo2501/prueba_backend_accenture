package com.accenture.franchise.infrastructure.persistence.repository;

import com.accenture.franchise.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, Long> {
    Flux<ProductEntity> findByBranchId(Long branchId);
}
