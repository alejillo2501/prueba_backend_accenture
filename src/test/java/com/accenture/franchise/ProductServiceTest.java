package com.accenture.franchise;

import com.accenture.franchise.application.dto.ProductDTO;
import com.accenture.franchise.application.service.ProductService;
import com.accenture.franchise.domain.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.BranchRepository;
import com.accenture.franchise.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Branch branch;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        branch = Branch.builder().id(10L).name("Downtown").franchiseId(1L).build();
        product = Product.builder().id(100L).name("Laptop").stock(10).precio(BigDecimal.valueOf(1200)).branchId(10L).build();
        productDTO = ProductDTO.builder().name("New Product").stock(20).precio(BigDecimal.TEN).branchId(10L).build();
    }

    @Test
    void getProduct_shouldReturnProduct_whenExists() {
        when(productRepository.findById(100L)).thenReturn(Mono.just(product));

        StepVerifier.create(productService.getProduct(100L))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void getProduct_shouldThrowException_whenNotExists() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProduct(99L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void createProduct_shouldCreateProduct_whenBranchExists() {
        when(branchRepository.findById(10L)).thenReturn(Mono.just(branch));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.createProduct(productDTO))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void createProduct_shouldThrowException_whenBranchNotExists() {
        when(branchRepository.findById(10L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.createProduct(productDTO))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void deleteProduct_shouldComplete_whenProductExists() {
        when(productRepository.findById(100L)).thenReturn(Mono.just(product));
        when(productRepository.deleteById(100L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProduct(100L))
                .verifyComplete();
    }
}