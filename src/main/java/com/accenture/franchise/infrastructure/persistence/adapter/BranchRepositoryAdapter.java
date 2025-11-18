package com.accenture.franchise.infrastructure.persistence.adapter;

import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.BranchRepository;
import com.accenture.franchise.infrastructure.persistence.entity.BranchEntity;
import com.accenture.franchise.infrastructure.persistence.entity.ProductEntity;
import com.accenture.franchise.infrastructure.persistence.repository.BranchR2dbcRepository;
import com.accenture.franchise.infrastructure.persistence.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {
    private final BranchR2dbcRepository branchRepository;
    private final ProductR2dbcRepository productRepository;
    
    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(this::loadProductsAndConvertToDomain);
    }

    @Override
    public Flux<Branch> findAll() {
        return branchRepository.findAll()
                .flatMap(this::loadProductsAndConvertToDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return branchRepository.findById(id)
                .flatMap(this::loadProductsAndConvertToDomain);
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchEntity entity = BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
        return branchRepository.save(entity)
                .map(this::toDomainModel);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return branchRepository.deleteById(id);
    }
    
    private Mono<Branch> loadProductsAndConvertToDomain(BranchEntity branchEntity) {
        return productRepository.findByBranchId(branchEntity.getId())
                .collectList()
                .map(productEntities -> {
                    branchEntity.setProducts(productEntities);
                    return toDomainModel(branchEntity);
                });
    }
    
    private Branch toDomainModel(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .franchiseId(entity.getFranchiseId())
                .name(entity.getName())
                .products(entity.getProducts() != null ? 
                         entity.getProducts().stream().map(this::toProductModel).toList() : 
                         new ArrayList<>())
                .build();
    }
    
    private Product toProductModel(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .branchId(entity.getBranchId())
                .name(entity.getName())
                .stock(entity.getStock())
                .precio(entity.getPrecio())
                .build();
    }
}
