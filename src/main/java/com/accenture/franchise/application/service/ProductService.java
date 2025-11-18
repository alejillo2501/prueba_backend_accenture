package com.accenture.franchise.application.service;

import com.accenture.franchise.application.dto.ProductDTO;
import com.accenture.franchise.domain.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.BranchRepository;
import com.accenture.franchise.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<Product> getProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product", id)));
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> createProduct(ProductDTO productDTO) {
        return branchRepository.findById(productDTO.getBranchId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch", productDTO.getBranchId())))
                .flatMap(branch -> {
                    Product product = Product.builder()
                            .name(productDTO.getName())
                            .stock(productDTO.getStock())
                            .precio(productDTO.getPrecio())
                            .branchId(branch.getId())
                            .build();
                    return productRepository.save(product);
                });
    }

    public Mono<Product> updateProduct(Long id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product", id)))
                .flatMap(existingProduct -> {
                    existingProduct.setName(productDTO.getName());
                    existingProduct.setStock(productDTO.getStock());
                    existingProduct.setPrecio(productDTO.getPrecio());
                    // Opcional: Validar si la sucursal existe si se permite cambiarla
                    // existingProduct.setBranchId(productDTO.getBranchId());
                    return productRepository.save(existingProduct);
                });
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product", id)))
                .flatMap(product -> productRepository.deleteById(id));
    }
}