package com.accenture.franchise.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseDTO {
    private Long id;
    private String name;
    private List<BranchDTO> branches;
}
