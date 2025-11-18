package com.accenture.franchise.domain.repository;

import com.accenture.franchise.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Flux<Product> findByBranchId(Long branchId);
    Flux<Product> findAll();
    Mono<Product> findById(Long id);
    Mono<Product> save(Product product);
    Mono<Void> deleteById(Long id);
}