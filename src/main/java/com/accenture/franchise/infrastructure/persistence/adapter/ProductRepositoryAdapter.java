package com.accenture.franchise.infrastructure.persistence.adapter;

import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.ProductRepository;
import com.accenture.franchise.infrastructure.persistence.entity.ProductEntity;
import com.accenture.franchise.infrastructure.persistence.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductR2dbcRepository productR2dbcRepository;

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return productR2dbcRepository.findByBranchId(branchId)
                .map(this::toDomainModel);
    }

    @Override
    public Flux<Product> findAll() {
        return productR2dbcRepository.findAll()
                .map(this::toDomainModel);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return productR2dbcRepository.findById(id)
                .map(this::toDomainModel);
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .precio(product.getPrecio())
                .branchId(product.getBranchId())
                .build();
        return productR2dbcRepository.save(entity)
                .map(this::toDomainModel);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productR2dbcRepository.deleteById(id);
    }

    private Product toDomainModel(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .precio(entity.getPrecio())
                .branchId(entity.getBranchId())
                .build();
    }
}