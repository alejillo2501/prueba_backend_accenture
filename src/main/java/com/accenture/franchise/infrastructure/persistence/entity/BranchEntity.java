package com.accenture.franchise.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("branches")
public class BranchEntity {
    @Id
    private Long id;
    private Long franchiseId;
    private String name;

    @Transient // No persistir en BD
    private List<ProductEntity> products = new ArrayList<>();
}
