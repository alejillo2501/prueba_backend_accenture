package com.accenture.franchise.application.service;

import com.accenture.franchise.application.dto.FranchiseDTO;
import com.accenture.franchise.application.dto.ProductDTO;
import com.accenture.franchise.application.dto.BranchDTO;
import com.accenture.franchise.domain.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.BranchRepository;
import com.accenture.franchise.domain.repository.ProductRepository;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    
    public Mono<FranchiseDTO> createFranchise(FranchiseDTO franchiseDTO) {
        Franchise franchise = Franchise.builder()
                .name(franchiseDTO.getName())
                .build();
        
        return franchiseRepository.save(franchise)
                .map(this::toDTO);
    }

    public Flux<FranchiseDTO> getAllFranchises() {
        return franchiseRepository.findAll()
                .map(this::toDTO);
    }

    public Mono<FranchiseDTO> getFranchiseById(Long id) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise", id)))
                .map(this::toDTO);
    }

    public Mono<Void> deleteFranchise(Long id) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise", id)))
                .flatMap(franchise -> franchiseRepository.deleteById(id));
    }
    
    public Mono<FranchiseDTO> updateFranchiseName(Long id, String newName) {
        return franchiseRepository.updateName(id, newName)
                .map(this::toDTO);
    }
    
    public Flux<ProductDTO> getMaxStockProductsByFranchise(Long franchiseId) {
        return franchiseRepository.findById(franchiseId) // Asegura que la franquicia exista
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise", franchiseId)))
                .flatMapMany(franchise -> branchRepository.findByFranchiseId(franchise.getId())) // Obtiene todas las sucursales
                .flatMap(branch -> productRepository.findByBranchId(branch.getId())) // Obtiene todos los productos de todas las sucursales
                .collectList() // Agrupa todos los productos en una sola lista
                .flatMapMany(allProducts -> {
                    if (allProducts.isEmpty()) {
                        return Flux.empty();
                    }
                    // Calcula el stock máximo de la lista completa
                    Integer maxStock = allProducts.stream()
                            .map(Product::getStock)
                            .max(Comparator.naturalOrder())
                            .orElse(0);
                    // Filtra y devuelve los productos que tienen el stock máximo
                    return Flux.fromIterable(allProducts)
                            .filter(product -> product.getStock().equals(maxStock));
                })
                .map(this::toProductDTO);
    }
    
    private FranchiseDTO toDTO(Franchise franchise) {
        return FranchiseDTO.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(franchise.getBranches() != null ? franchise.getBranches().stream()
                        .map(this::toBranchDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    private BranchDTO toBranchDTO(Branch branch) {
        return BranchDTO.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(branch.getProducts() != null ? branch.getProducts().stream()
                        .map(this::toProductDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    private ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .precio(product.getPrecio())
                .build();
    }
}
