package com.accenture.franchise.web.controller;

import com.accenture.franchise.application.dto.BranchDTO;
import com.accenture.franchise.application.service.BranchService;
import com.accenture.franchise.domain.model.Branch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Gestión de sucursales")
public class BranchController {

    private final BranchService branchService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una sucursal por su ID")
    public Mono<ResponseEntity<Branch>> getBranch(@PathVariable Long id) {
        return branchService.getBranch(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales")
    public Flux<Branch> getAllBranches() {
        return branchService.getAllBranches();
    }
    @PostMapping
    @Operation(summary = "Crear una nueva sucursal")
    public Mono<ResponseEntity<Branch>> createBranch(@Valid @RequestBody BranchDTO branchDTO) {
        return branchService.createBranch(branchDTO)
                .map(branch -> ResponseEntity.status(HttpStatus.CREATED).body(branch));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal")
    public Mono<ResponseEntity<Branch>> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchDTO branchDTO) {
        return branchService.updateBranch(id, branchDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una sucursal")
    public Mono<Void> deleteBranch(@PathVariable Long id) {
        return branchService.deleteBranch(id);
    }
}