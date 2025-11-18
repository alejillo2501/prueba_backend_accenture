package com.accenture.franchise.domain.repository;

import com.accenture.franchise.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Flux<Branch> findByFranchiseId(Long franchiseId);
    Flux<Branch> findAll();
    Mono<Branch> findById(Long id);
    Mono<Branch> save(Branch branch);
    Mono<Void> deleteById(Long id);
}