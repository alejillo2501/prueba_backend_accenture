package com.accenture.franchise.infrastructure.persistence.adapter;

import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import com.accenture.franchise.infrastructure.persistence.entity.FranchiseEntity;
import com.accenture.franchise.infrastructure.persistence.entity.BranchEntity;
import com.accenture.franchise.infrastructure.persistence.entity.ProductEntity;
import com.accenture.franchise.infrastructure.persistence.repository.FranchiseR2dbcRepository;
import com.accenture.franchise.infrastructure.persistence.repository.BranchR2dbcRepository;
import com.accenture.franchise.infrastructure.persistence.repository.ProductR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseR2dbcRepository franchiseRepository;
    private final BranchR2dbcRepository branchRepository;
    private final ProductR2dbcRepository productRepository;
    
    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = FranchiseEntity.builder()
                .name(franchise.getName())
                .build();
        
        return franchiseRepository.save(entity)
                .map(this::toDomainModel);
    }
    
    @Override
    public Mono<Franchise> findById(Long id) {
        return franchiseRepository.findById(id)
                .flatMap(this::loadChildrenAndConvertToDomain);
    }
    
    @Override
    public Flux<Franchise> findAll() {
        return franchiseRepository.findAll()
                .flatMap(this::loadChildrenAndConvertToDomain);
    }
    
    @Override
    public Mono<Void> deleteById(Long id) {
        // Se podría añadir lógica para borrar en cascada sucursales y productos si fuera necesario.
        return franchiseRepository.deleteById(id);
    }
    
    @Override
    public Mono<Franchise> updateName(Long id, String newName) {
        return franchiseRepository.updateName(id, newName)
                .map(this::toDomainModel);
    }
    
    private Mono<Franchise> loadChildrenAndConvertToDomain(FranchiseEntity franchiseEntity) {
        return branchRepository.findByFranchiseId(franchiseEntity.getId())
                .flatMap(this::loadProductsForBranch)
                .collectList()
                .map(branchEntities -> {
                    franchiseEntity.setBranches(branchEntities);
                    return toDomainModel(franchiseEntity);
                });
    }
    
    private Mono<BranchEntity> loadProductsForBranch(BranchEntity branch) {
        return productRepository.findByBranchId(branch.getId())
                .collectList()
                .map(products -> {
                    branch.setProducts(products);
                    return branch;
                });
    }
    
    private Franchise toDomainModel(FranchiseEntity entity) {
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .branches(entity.getBranches() != null ?
                        entity.getBranches().stream().map(this::toBranchModel).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    private Branch toBranchModel(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .franchiseId(entity.getFranchiseId())
                .name(entity.getName())
                .products(entity.getProducts() != null ?
                        entity.getProducts().stream().map(this::toProductModel).collect(Collectors.toList())
                        : new ArrayList<>())
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
