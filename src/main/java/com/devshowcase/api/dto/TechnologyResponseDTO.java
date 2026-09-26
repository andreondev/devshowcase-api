package com.devshowcase.api.dto;

import com.devshowcase.api.model.Technology;

public record TechnologyResponseDTO(
    Long id, 
    String name
) {
    public static TechnologyResponseDTO fromEntity(Technology tech) {
        return new TechnologyResponseDTO(tech.getId(), tech.getName());
    }
}