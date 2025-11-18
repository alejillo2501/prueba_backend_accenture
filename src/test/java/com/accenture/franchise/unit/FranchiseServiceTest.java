package com.accenture.franchise.unit;

import com.accenture.franchise.application.dto.FranchiseDTO;
import com.accenture.franchise.application.service.FranchiseService;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FranchiseServiceTest {
    @Mock
    private FranchiseRepository franchiseRepository;
    
    @InjectMocks
    private FranchiseService franchiseService;
    
    private Franchise testFranchise;
    private FranchiseDTO testFranchiseDTO;
    
    @BeforeEach
    void setUp() {
        testFranchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
        
        testFranchiseDTO = FranchiseDTO.builder()
                .name("Test Franchise")
                .build();
    }
    
    @Test
    void createFranchise_ShouldReturnCreatedFranchise() {
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(testFranchise));
        
        StepVerifier.create(franchiseService.createFranchise(testFranchiseDTO))
                .expectNextMatches(dto -> dto.getId().equals(1L) && dto.getName().equals("Test Franchise"))
                .verifyComplete();
    }
}
