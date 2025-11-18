package com.accenture.franchise.infrastructure.persistence.repository;

import com.accenture.franchise.infrastructure.persistence.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface FranchiseR2dbcRepository extends R2dbcRepository<FranchiseEntity, Long> {
    @Query("UPDATE franchises SET name = :newName WHERE id = :id RETURNING *")
    Mono<FranchiseEntity> updateName(@Param("id") Long id, @Param("newName") String newName);
}
