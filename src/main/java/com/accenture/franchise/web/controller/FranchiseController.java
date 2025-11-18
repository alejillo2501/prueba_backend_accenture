package com.accenture.franchise.web.controller;

import com.accenture.franchise.application.dto.FranchiseDTO;
import com.accenture.franchise.application.dto.ProductDTO;
import com.accenture.franchise.application.service.FranchiseService;
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
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises", description = "Gestión de franquicias")
public class FranchiseController {
    private final FranchiseService franchiseService;

    @GetMapping
    @Operation(summary = "Obtener todas las franquicias")
    public Flux<FranchiseDTO> getAllFranchises() {
        return franchiseService.getAllFranchises();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una franquicia por su ID")
    public Mono<ResponseEntity<FranchiseDTO>> getFranchiseById(@PathVariable Long id) {
        return franchiseService.getFranchiseById(id)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una franquicia")
    public Mono<Void> deleteFranchise(@PathVariable Long id) {
        return franchiseService.deleteFranchise(id);
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva franquicia")
    public Mono<ResponseEntity<FranchiseDTO>> createFranchise(@Valid @RequestBody FranchiseDTO franchiseDTO) {
        return franchiseService.createFranchise(franchiseDTO)
                .map(franchise -> ResponseEntity.status(HttpStatus.CREATED).body(franchise));
    }
    
    @PutMapping("/{id}/name")
    @Operation(summary = "Actualizar nombre de la franquicia")
    public Mono<ResponseEntity<FranchiseDTO>> updateFranchiseName(@PathVariable Long id, @RequestParam String newName) {
        return franchiseService.updateFranchiseName(id, newName)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/{id}/max-stock-products")
    @Operation(summary = "Obtener productos con mayor stock por sucursal")
    public Flux<ProductDTO> getMaxStockProducts(@PathVariable Long id) {
        return franchiseService.getMaxStockProductsByFranchise(id);
    }

}
