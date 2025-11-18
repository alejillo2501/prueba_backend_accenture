package com.accenture.franchise.application.service;

import com.accenture.franchise.domain.exception.ResourceNotFoundException;
import com.accenture.franchise.application.dto.BranchDTO;
import com.accenture.franchise.domain.model.Branch;
import com.accenture.franchise.domain.repository.BranchRepository;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> getBranch(Long id) {
        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch", id)));
    }

    public Flux<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Mono<Branch> createBranch(BranchDTO branchDTO) {
        return franchiseRepository.findById(branchDTO.getFranchiseId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise", branchDTO.getFranchiseId())))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .name(branchDTO.getName())
                            .franchiseId(franchise.getId())
                            .build();
                    return branchRepository.save(branch);
                });
    }

    public Mono<Branch> updateBranch(Long id, BranchDTO branchDTO) {
        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch", id)))
                .flatMap(existingBranch -> {
                    existingBranch.setName(branchDTO.getName());
                    // Opcional: Validar si la franquicia existe si se permite cambiarla
                    // existingBranch.setFranchiseId(branchDTO.getFranchiseId());
                    return branchRepository.save(existingBranch);
                });
    }

    public Mono<Void> deleteBranch(Long id) {
        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch", id)))
                .flatMap(branch -> branchRepository.deleteById(id));
    }

}