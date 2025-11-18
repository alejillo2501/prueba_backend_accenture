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
@Table("franchises")

public class Franchise {
    @Id
    private Long id;
    
    @NotBlank(message = "El nombre de la franquicia es obligatorio")
    private String name;
    
    private List<Branch> branches;
}
