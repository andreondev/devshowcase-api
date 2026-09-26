package com.devshowcase.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import java.util.Set;

public record ProjectRequestDTO(
    @NotBlank(message = "O título é obrigatório") 
    String title,
    
    @NotBlank(message = "A descrição é obrigatória") 
    String description,
    
    @URL(message = "Forneça um URL de repositório válido") 
    String repositoryUrl,
    
    @NotNull(message = "O ID do perfil é obrigatório") 
    Long profileId,
    
    @NotEmpty(message = "Informe ao menos um ID de tecnologia") 
    Set<Long> technologyIds
) {}