package com.devshowcase.api.dto;

import com.devshowcase.api.model.Profile;

public record ProfileResponseDTO(
    Long id,
    String name, 
    String email, 
    String githubUrl
) {
    // Método utilitário para converter uma entidade Profile em DTO de Saída
    public static ProfileResponseDTO fromEntity(Profile profile) {
        return new ProfileResponseDTO(
            profile.getId(),
            profile.getName(),
            profile.getEmail(),
            profile.getGithubUrl()
        );
    }
}