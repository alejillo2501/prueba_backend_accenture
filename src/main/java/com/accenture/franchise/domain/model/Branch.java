package com.accenture.franchise.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("branches")
public class Branch {
    @Id
    private Long id;
    
    private Long franchiseId;
    
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String name;
    
    private List<Product> products;
}
