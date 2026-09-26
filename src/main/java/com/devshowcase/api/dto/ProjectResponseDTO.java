package com.devshowcase.api.dto;

import com.devshowcase.api.model.Project;
import java.util.Set;
import java.util.stream.Collectors;

public record ProjectResponseDTO(
    Long id, 
    String title, 
    String description, 
    String repositoryUrl, 
    Long profileId, 
    Set<String> technologies,
    Integer upvotes,
    Double averageRating
) {
    public static ProjectResponseDTO fromEntity(Project project) {
        // Converte a lista de entidades Technology para uma lista apenas com os nomes das tecnologias
        Set<String> techNames = project.getTechnologies().stream()
                .map(tech -> tech.getName())
                .collect(Collectors.toSet());

        return new ProjectResponseDTO(
            project.getId(),
            project.getTitle(),
            project.getDescription(),
            project.getRepositoryUrl(),
            project.getProfile().getId(),
            techNames,
            project.getUpvotes(),
            project.getAverageRating()
        );
    }
}