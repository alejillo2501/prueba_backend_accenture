package com.accenture.franchise;

import com.accenture.franchise.application.dto.BranchDTO;
import com.accenture.franchise.application.service.BranchService;
import com.accenture.franchise.domain.exception.ResourceNotFoundException;
import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.repository.BranchRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch branch;
    private Franchise franchise;
    private BranchDTO branchDTO;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder().id(1L).name("TechStore").build();
        branch = Branch.builder().id(10L).name("Downtown").franchiseId(1L).build();
        branchDTO = BranchDTO.builder().name("New Branch").franchiseId(1L).build();
    }

    @Test
    void getBranch_shouldReturnBranch_whenExists() {
        when(branchRepository.findById(10L)).thenReturn(Mono.just(branch));

        StepVerifier.create(branchService.getBranch(10L))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void getBranch_shouldThrowException_whenNotExists() {
        when(branchRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(branchService.getBranch(99L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void createBranch_shouldCreateBranch_whenFranchiseExists() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));

        StepVerifier.create(branchService.createBranch(branchDTO))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void createBranch_shouldThrowException_whenFranchiseNotExists() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.createBranch(branchDTO))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void deleteBranch_shouldComplete_whenBranchExists() {
        when(branchRepository.findById(10L)).thenReturn(Mono.just(branch));
        when(branchRepository.deleteById(10L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.deleteBranch(10L))
                .verifyComplete();
    }
}