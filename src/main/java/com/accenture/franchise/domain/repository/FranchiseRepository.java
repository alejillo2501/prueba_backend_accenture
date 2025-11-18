package com.accenture.franchise.domain.repository;

import com.accenture.franchise.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Flux<Franchise> findAll();
    Mono<Void> deleteById(Long id);
    Mono<Franchise> updateName(Long id, String newName);
}
