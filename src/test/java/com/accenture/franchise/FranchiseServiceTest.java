package com.accenture.franchise;

import com.accenture.franchise.application.service.FranchiseService;
import com.accenture.franchise.domain.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.model.Product;
import com.accenture.franchise.domain.repository.BranchRepository;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import com.accenture.franchise.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FranchiseService franchiseService;

    private Franchise franchise;
    private Branch branch1, branch2;
    private Product product1, product2, product3, product4;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder().id(1L).name("TechStore").build();

        branch1 = Branch.builder().id(10L).franchiseId(1L).name("Downtown").build();
        branch2 = Branch.builder().id(11L).franchiseId(1L).name("Uptown").build();

        product1 = Product.builder().id(100L).name("Laptop").stock(10).precio(BigDecimal.valueOf(1200)).branchId(10L).build();
        product2 = Product.builder().id(101L).name("Mouse").stock(50).precio(BigDecimal.valueOf(25)).branchId(10L).build();
        product3 = Product.builder().id(102L).name("Keyboard").stock(50).precio(BigDecimal.valueOf(75)).branchId(11L).build();
        product4 = Product.builder().id(103L).name("Monitor").stock(20).precio(BigDecimal.valueOf(300)).branchId(11L).build();

        franchise.setBranches(List.of(branch1, branch2));
        branch1.setProducts(List.of(product1, product2));
        branch2.setProducts(List.of(product3, product4));
    }

    @Test
    void getFranchiseById_shouldReturnFranchise_whenExists() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseService.getFranchiseById(1L))
                .expectNextMatches(franchiseDTO ->
                        franchiseDTO.getName().equals("TechStore") &&
                        franchiseDTO.getBranches().size() == 2)
                .verifyComplete();
    }

    @Test
    void getFranchiseById_shouldThrowException_whenNotExists() {
        when(franchiseRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.getFranchiseById(99L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void getAllFranchises_shouldReturnAllFranchises() {
        when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise));

        StepVerifier.create(franchiseService.getAllFranchises())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void deleteFranchise_shouldComplete_whenFranchiseExists() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.deleteFranchise(1L))
                .verifyComplete();
    }

    @Test
    void getMaxStockProductsByFranchise_shouldReturnProductsWithMaxStock() {
        // Arrange
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepository.findByFranchiseId(1L)).thenReturn(Flux.just(branch1, branch2));
        when(productRepository.findByBranchId(10L)).thenReturn(Flux.just(product1, product2)); // product2 has stock 50
        when(productRepository.findByBranchId(11L)).thenReturn(Flux.just(product3, product4)); // product3 has stock 50

        // Act & Assert
        StepVerifier.create(franchiseService.getMaxStockProductsByFranchise(1L))
                .expectNextMatches(p -> p.getName().equals("Mouse") && p.getStock() == 50)
                .expectNextMatches(p -> p.getName().equals("Keyboard") && p.getStock() == 50)
                .verifyComplete();
    }

    @Test
    void getMaxStockProductsByFranchise_shouldReturnEmpty_whenNoProducts() {
        // Arrange
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepository.findByFranchiseId(1L)).thenReturn(Flux.just(branch1));
        when(productRepository.findByBranchId(10L)).thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(franchiseService.getMaxStockProductsByFranchise(1L))
                .verifyComplete();
    }
}