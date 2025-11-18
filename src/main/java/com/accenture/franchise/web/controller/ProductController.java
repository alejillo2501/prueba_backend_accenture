package com.accenture.franchise.web.controller;

import com.accenture.franchise.application.dto.ProductDTO;
import com.accenture.franchise.application.service.ProductService;
import com.accenture.franchise.domain.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Gestión de productos")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    public Mono<ResponseEntity<Product>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO)
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un producto")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}